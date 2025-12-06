package com.malsync.android.data.repository

import com.malsync.android.data.local.dao.AnimeDao
import com.malsync.android.data.mapper.toDomain
import com.malsync.android.data.mapper.toEntity
import com.malsync.android.data.mapper.toMalStatus
import com.malsync.android.data.remote.api.MalApiService
import com.malsync.android.domain.model.*
import com.malsync.android.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val malApiService: MalApiService,
    private val animeDao: AnimeDao
) : AnimeRepository {
    
    override fun getAnimeList(provider: SyncProvider, status: UserAnimeStatus?): Flow<List<Anime>> {
        return if (status != null) {
            animeDao.getAnimeByProviderAndStatus(provider.name, status.name)
        } else {
            animeDao.getAnimeByProvider(provider.name)
        }.map { entities -> entities.map { it.toDomain() } }
    }
    
    override suspend fun getAnime(provider: SyncProvider, id: String): Result<Anime> {
        return try {
            // Try to get from local cache first
            val cached = animeDao.getAnimeById(id)
            if (cached != null) {
                return Result.success(cached.toDomain())
            }
            
            // Fetch from remote
            when (provider) {
                SyncProvider.MYANIMELIST -> {
                    val response = malApiService.getAnimeDetails(id)
                    if (response.isSuccessful && response.body() != null) {
                        val anime = response.body()!!.toDomain(provider)
                        animeDao.insertAnime(anime.toEntity())
                        Result.success(anime)
                    } else {
                        Result.failure(Exception("Failed to fetch anime: ${response.code()}"))
                    }
                }
                else -> Result.failure(Exception("Provider not yet implemented"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchAnime(query: String, providers: List<SyncProvider>): Result<List<Anime>> {
        return try {
            // For now, just search MAL
            if (providers.contains(SyncProvider.MYANIMELIST)) {
                val response = malApiService.searchAnime(query)
                if (response.isSuccessful && response.body() != null) {
                    val animeList = response.body()!!.data.map { it.node.toDomain(SyncProvider.MYANIMELIST) }
                    Result.success(animeList)
                } else {
                    Result.failure(Exception("Search failed: ${response.code()}"))
                }
            } else {
                Result.failure(Exception("Provider not yet implemented"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateAnimeProgress(
        provider: SyncProvider,
        animeId: String,
        episode: Int,
        status: UserAnimeStatus?,
        score: Int?
    ): Result<Anime> {
        return try {
            when (provider) {
                SyncProvider.MYANIMELIST -> {
                    val response = malApiService.updateAnimeListStatus(
                        animeId = animeId,
                        status = status?.toMalStatus(),
                        score = score,
                        numWatchedEpisodes = episode
                    )
                    
                    if (response.isSuccessful) {
                        // Fetch updated anime details
                        getAnime(provider, animeId)
                    } else {
                        Result.failure(Exception("Update failed: ${response.code()}"))
                    }
                }
                else -> Result.failure(Exception("Provider not yet implemented"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun addAnimeToList(
        provider: SyncProvider,
        animeId: String,
        status: UserAnimeStatus
    ): Result<Anime> {
        return updateAnimeProgress(provider, animeId, 0, status, null)
    }
    
    override suspend fun removeAnimeFromList(provider: SyncProvider, animeId: String): Result<Unit> {
        return try {
            when (provider) {
                SyncProvider.MYANIMELIST -> {
                    val response = malApiService.deleteAnimeFromList(animeId)
                    if (response.isSuccessful) {
                        animeDao.deleteAnimeById(animeId)
                        Result.success(Unit)
                    } else {
                        Result.failure(Exception("Delete failed: ${response.code()}"))
                    }
                }
                else -> Result.failure(Exception("Provider not yet implemented"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun syncAnimeList(provider: SyncProvider): Result<Unit> {
        return try {
            when (provider) {
                SyncProvider.MYANIMELIST -> {
                    val response = malApiService.getUserAnimeList()
                    if (response.isSuccessful && response.body() != null) {
                        val animeList = response.body()!!.data.map { it.toDomain() }
                        
                        // Clear old data and insert new
                        animeDao.deleteAllByProvider(provider.name)
                        animeDao.insertAllAnime(animeList.map { it.toEntity() })
                        
                        Result.success(Unit)
                    } else {
                        Result.failure(Exception("Sync failed: ${response.code()}"))
                    }
                }
                else -> Result.failure(Exception("Provider not yet implemented"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAnimeByUrl(url: String): Result<Anime?> {
        // This will be implemented with URL detection logic
        return try {
            // TODO: Implement URL pattern matching and anime detection
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
