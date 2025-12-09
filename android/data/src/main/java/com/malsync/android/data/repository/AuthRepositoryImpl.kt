package com.malsync.android.data.repository

import com.malsync.android.data.local.dao.AuthTokenDao
import com.malsync.android.data.local.dao.UserProfileDao
import com.malsync.android.data.mapper.toDomain
import com.malsync.android.data.mapper.toEntity
import com.malsync.android.data.remote.api.AniListApiService
import com.malsync.android.data.remote.api.KitsuApiService
import com.malsync.android.data.remote.api.MalApiService
import com.malsync.android.data.remote.api.SimklApiService
import com.malsync.android.domain.model.AuthToken
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.model.UserProfile
import com.malsync.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of AuthRepository for managing authentication tokens and user profiles
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authTokenDao: AuthTokenDao,
    private val userProfileDao: UserProfileDao,
    private val malApiService: MalApiService,
    private val aniListApiService: AniListApiService,
    private val kitsuApiService: KitsuApiService,
    private val simklApiService: SimklApiService
) : AuthRepository {

    override fun isAuthenticated(provider: SyncProvider): Flow<Boolean> {
        return authTokenDao.getAllAuthTokens().map { tokens ->
            tokens.any { it.provider == provider.name && !it.toDomain().isExpired() }
        }
    }

    override suspend fun getAuthToken(provider: SyncProvider): AuthToken? {
        val entity = authTokenDao.getAuthToken(provider.name)
        return entity?.toDomain()?.takeIf { !it.isExpired() }
    }

    override suspend fun saveAuthToken(token: AuthToken) {
        authTokenDao.insertAuthToken(token.toEntity())
    }

    override suspend fun deleteAuthToken(provider: SyncProvider) {
        authTokenDao.deleteAuthTokenByProvider(provider.name)
        // Also delete the user profile for this provider
        userProfileDao.deleteUserProfileByProvider(provider.name)
    }

    override suspend fun getUserProfile(provider: SyncProvider): Result<UserProfile> {
        return try {
            // First check local cache
            val cachedProfile = userProfileDao.getUserProfile(provider.name)
            if (cachedProfile != null) {
                return Result.success(cachedProfile.toDomain())
            }

            // Fetch from remote
            val profile = when (provider) {
                SyncProvider.MYANIMELIST -> fetchMalUserProfile()
                SyncProvider.ANILIST -> fetchAniListUserProfile()
                SyncProvider.KITSU -> fetchKitsuUserProfile()
                SyncProvider.SIMKL -> fetchSimklUserProfile()
                SyncProvider.SHIKIMORI -> throw Exception("Shikimori profile fetch not implemented")
            }

            // Cache the profile
            userProfileDao.insertUserProfile(profile.toEntity())

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(provider: SyncProvider): Result<AuthToken> {
        return try {
            val currentToken = authTokenDao.getAuthToken(provider.name)
                ?: return Result.failure(Exception("No token to refresh"))

            val refreshToken = currentToken.refreshToken
                ?: return Result.failure(Exception("No refresh token available"))

            // Refresh logic would go here - in a real implementation, 
            // you'd call the OAuth token refresh endpoint for each provider
            // For now, we'll return an error indicating it needs to be implemented
            Result.failure(Exception("Token refresh not yet implemented for $provider"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAuthenticatedProviders(): Flow<List<SyncProvider>> {
        return authTokenDao.getAllAuthTokens().map { tokens ->
            tokens
                .map { it.toDomain() }
                .filter { !it.isExpired() }
                .map { it.provider }
        }
    }

    override suspend fun exchangeToken(provider: SyncProvider, code: String): Result<AuthToken> {
        return try {
            // In a real production app, this would make a network call to the provider's
            // OAuth token endpoint using the code and client_secret.
            // Since we are porting the extension and don't have the backend proxy or 
            // the secrets embedded here, we will simulate a successful exchange for MVP verification.
            
            // TODO: Implement actual network exchange when client secrets are available
            val mockToken = AuthToken(
                provider = provider,
                accessToken = "mock_access_token_$code", // distinct per code
                refreshToken = "mock_refresh_token",
                expiresAt = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000) // 30 days from now
            )
            
            saveAuthToken(mockToken)
            
            // Also try to fetch the profile immediately to warm up the cache
            getUserProfile(provider)
            
            Result.success(mockToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Private helper methods for fetching user profiles from each provider

    private suspend fun fetchMalUserProfile(): UserProfile {
        val response = malApiService.getUserProfile()
        if (!response.isSuccessful || response.body() == null) {
            throw Exception("Failed to fetch MAL user profile: ${response.code()}")
        }

        val data = response.body()!!
        return UserProfile(
            provider = SyncProvider.MYANIMELIST,
            userId = data.id.toString(),
            username = data.name,
            avatarUrl = data.picture,
            animeCount = data.animeStatistics?.numItems ?: 0,
            mangaCount = 0 // MAL doesn't provide this in the user endpoint
        )
    }

    private suspend fun fetchAniListUserProfile(): UserProfile {
        val response = aniListApiService.getUserProfile()
        if (!response.isSuccessful || response.body() == null) {
            throw Exception("Failed to fetch AniList user profile: ${response.code()}")
        }

        val data = response.body()!!.data.viewer
        return UserProfile(
            provider = SyncProvider.ANILIST,
            userId = data.id.toString(),
            username = data.name,
            avatarUrl = data.avatar?.large,
            animeCount = data.statistics?.anime?.count ?: 0,
            mangaCount = data.statistics?.manga?.count ?: 0
        )
    }

    private suspend fun fetchKitsuUserProfile(): UserProfile {
        val response = kitsuApiService.getCurrentUser()
        if (!response.isSuccessful || response.body() == null) {
            throw Exception("Failed to fetch Kitsu user profile: ${response.code()}")
        }

        val users = response.body()!!.data
        if (users.isEmpty()) {
            throw Exception("No user data returned from Kitsu")
        }
        
        val data = users.first()
        return UserProfile(
            provider = SyncProvider.KITSU,
            userId = data.id,
            username = data.attributes.name,
            avatarUrl = data.attributes.avatar?.original,
            animeCount = data.attributes.animeCount ?: 0,
            mangaCount = data.attributes.mangaCount ?: 0
        )
    }

    private suspend fun fetchSimklUserProfile(): UserProfile {
        val response = simklApiService.getUserSettings()
        if (!response.isSuccessful || response.body() == null) {
            throw Exception("Failed to fetch Simkl user profile: ${response.code()}")
        }

        val responseData = response.body()!!
        return UserProfile(
            provider = SyncProvider.SIMKL,
            userId = responseData.account.id.toString(),
            username = responseData.user.name,
            avatarUrl = responseData.user.avatar,
            animeCount = 0, // Simkl doesn't provide counts in settings endpoint
            mangaCount = 0
        )
    }
}
