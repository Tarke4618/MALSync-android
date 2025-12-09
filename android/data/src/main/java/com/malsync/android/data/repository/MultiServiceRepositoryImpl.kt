package com.malsync.android.data.repository

import com.malsync.android.data.local.AuthManager
import com.malsync.android.data.remote.api.*
import com.malsync.android.domain.model.*
import com.malsync.android.domain.repository.MultiServiceRepository
import com.malsync.android.data.mapper.toUserAnimeListEntry
import com.malsync.android.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MultiServiceRepositoryImpl @Inject constructor(
    private val authManager: AuthManager,
    private val malApi: MalApiService,
    private val anilistApi: AniListApiService,
    private val kitsuApi: KitsuApiService,
    private val simklApi: SimklApiService,
    private val shikimoriApi: ShikimoriApiService
) : MultiServiceRepository {

    override fun getAnimeDetails(id: String): Flow<Anime> = flow {
        // Implementation TODO: Fetch from primary or fallback
        // For now emitting empty/error or fetching from MAL if ID matches
    }

    override fun searchAnime(query: String): Flow<List<Anime>> = flow {
        if (query.length < 3) {
            emit(emptyList())
            return@flow
        }
        
        try {
            // MVP Strategy: Search MAL as the "Primary Database"
            // In future, could search all and merge, but MAL is sufficient for "discovery"
            val response = malApi.searchAnime(query = query, limit = 20)
            if (response.isSuccessful) {
                val results = response.body()?.data?.map { it.node.toDomain(SyncProvider.MYANIMELIST) } ?: emptyList()
                emit(results)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    override fun getUserAnimeList(): Flow<List<UserAnimeListEntry>> = flow {
        // ... (existing implementation)
        emit(emptyList()) // Placeholder remains
    }

    override suspend fun fetchUserAnimeList(provider: SyncProvider): List<UserAnimeListEntry> {
        if (!authManager.isAuthenticated(provider)) return emptyList()
        
        return try {
            when (provider) {
                SyncProvider.MYANIMELIST -> {
                    val response = malApi.getUserAnimeList(status = "all", sort = "list_updated_at")
                    if (response.isSuccessful) {
                        response.body()?.data?.mapNotNull { it.toUserAnimeListEntry() } ?: emptyList()
                    } else emptyList()
                }
                SyncProvider.ANILIST -> {
                    // Fetch Viewer ID Strategy: 
                    // ideally cached, but fetching here for robustness
                    val profileResponse = anilistApi.getUserProfile()
                    val userId = profileResponse.body()?.data?.viewer?.id
                    
                    if (userId != null) {
                        val variables = mapOf(
                            "userId" to userId,
                            "type" to "ANIME"
                        )
                        val listResponse = anilistApi.getAnimeList(
                            mapOf("query" to AniListApiService.USER_ANIME_LIST_QUERY, "variables" to variables)
                        )
                        
                        if (listResponse.isSuccessful) {
                            listResponse.body()?.data?.mediaListCollection?.lists?.flatMap { group ->
                                group.entries.map { entry ->
                                    UserAnimeListEntry(
                                        animeId = entry.media?.id?.toString() ?: entry.mediaId.toString(),
                                        title = entry.media?.title?.romaji ?: entry.media?.title?.english ?: "Unknown",
                                        imageUrl = entry.media?.coverImage?.large ?: entry.media?.coverImage?.medium,
                                        status = mapAniListStatus(entry.status),
                                        score = entry.score,
                                        watchedEpisodes = entry.progress,
                                        totalEpisodes = entry.media?.episodes ?: 0,
                                        updatedAt = (entry.updatedAt ?: 0) * 1000L, // AniList is seconds
                                        provider = SyncProvider.ANILIST
                                    )
                                }
                            } ?: emptyList()
                        } else emptyList()
                    } else emptyList()
                }
                SyncProvider.KITSU -> {
                    val profileResponse = kitsuApi.getCurrentUser()
                    val userId = profileResponse.body()?.data?.firstOrNull()?.id
                    if (userId != null) {
                        val response = kitsuApi.getUserAnimeList(userId = userId)
                        if (response.isSuccessful) {
                            val data = response.body()?.data ?: emptyList()
                            val included = response.body()?.included ?: emptyList()
                            // Create map of ID -> KitsuAnime for O(1) lookup
                            val animeMap = included.associateBy { it.id }
                            
                            data.mapNotNull { entry ->
                                val animeId = entry.relationships?.anime?.data?.id
                                val anime = animeMap[animeId]
                                if (animeId != null) {
                                    UserAnimeListEntry(
                                        animeId = animeId,
                                        title = anime?.attributes?.canonicalTitle ?: "Unknown",
                                        imageUrl = anime?.attributes?.posterImage?.small ?: anime?.attributes?.posterImage?.medium,
                                        status = mapKitsuStatus(entry.attributes.status),
                                        score = (entry.attributes.rating?.toFloatOrNull() ?: 0f) * 2, // Kitsu 0.5-5.0 -> 1-10
                                        watchedEpisodes = entry.attributes.progress,
                                        totalEpisodes = anime?.attributes?.episodeCount ?: 0,
                                        updatedAt = System.currentTimeMillis(), // Kitsu date parsing needed, using current for fallback
                                        provider = SyncProvider.KITSU
                                    )
                                } else null
                            }
                        } else emptyList()
                    } else emptyList()
                }
                SyncProvider.SIMKL -> {
                    val response = simklApi.getUserAnimeList()
                    if (response.isSuccessful) {
                        response.body()?.anime?.map { item ->
                            UserAnimeListEntry(
                                animeId = item.show.ids.simkl.toString(),
                                title = item.show.title,
                                imageUrl = item.show.poster, // value is usually full url
                                status = mapSimklStatus(item.status),
                                score = item.userRating?.toFloat() ?: 0f,
                                watchedEpisodes = item.watchedEpisodesCount ?: 0,
                                totalEpisodes = item.totalEpisodesCount ?: item.show.totalEpisodes ?: 0,
                                updatedAt = System.currentTimeMillis(), // Parser needed
                                provider = SyncProvider.SIMKL
                            )
                        } ?: emptyList()
                    } else emptyList()
                }
                SyncProvider.SHIKIMORI -> {
                    val whoAmI = shikimoriApi.getWhoAmI()
                    val userId = whoAmI.body()?.id
                    if (userId != null) {
                        // Limit 1000 to get most
                        val response = shikimoriApi.getUserAnimeRates(userId = userId, limit = 1000)
                        if (response.isSuccessful) {
                            response.body()?.map { rate ->
                                UserAnimeListEntry(
                                    animeId = rate.targetId.toString(),
                                    title = "Anime #${rate.targetId}", // Missing in this endpoint
                                    imageUrl = null, // Missing in this endpoint
                                    status = mapShikimoriStatus(rate.status),
                                    score = rate.score.toFloat(),
                                    watchedEpisodes = rate.episodes,
                                    totalEpisodes = 0, // Unknown from rate
                                    updatedAt = System.currentTimeMillis(),
                                    provider = SyncProvider.SHIKIMORI
                                )
                            } ?: emptyList()
                        } else emptyList()
                    } else emptyList()
                }
                else -> emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    // Status Mappers
    
    private fun mapKitsuStatus(status: String?): UserAnimeStatus {
        return when (status?.lowercase()) {
            "current" -> UserAnimeStatus.WATCHING
            "planned" -> UserAnimeStatus.PLAN_TO_WATCH
            "completed" -> UserAnimeStatus.COMPLETED
            "dropped" -> UserAnimeStatus.DROPPED
            "on_hold" -> UserAnimeStatus.ON_HOLD
            else -> UserAnimeStatus.PLAN_TO_WATCH
        }
    }

    private fun mapSimklStatus(status: String?): UserAnimeStatus {
        return when (status?.lowercase()) {
            "watching" -> UserAnimeStatus.WATCHING
            "plantowatch" -> UserAnimeStatus.PLAN_TO_WATCH
            "completed" -> UserAnimeStatus.COMPLETED
            "dropped" -> UserAnimeStatus.DROPPED
            "hold" -> UserAnimeStatus.ON_HOLD
            else -> UserAnimeStatus.PLAN_TO_WATCH
        }
    }
    
    private fun mapShikimoriStatus(status: String?): UserAnimeStatus {
        return when (status?.lowercase()) {
            "watching" -> UserAnimeStatus.WATCHING
            "planned" -> UserAnimeStatus.PLAN_TO_WATCH
            "completed" -> UserAnimeStatus.COMPLETED
            "dropped" -> UserAnimeStatus.DROPPED
            "on_hold" -> UserAnimeStatus.ON_HOLD
            "rewatching" -> UserAnimeStatus.WATCHING
            else -> UserAnimeStatus.PLAN_TO_WATCH
        }
    }
    
    override suspend fun updateUserAnimeStatus(entry: UserAnimeListEntry, provider: SyncProvider) {
         // TODO: Implement update logic per provider
    }

    override suspend fun updateUserAnimeStatus(entry: UserAnimeListEntry) {
         // Implementation TODO: Update all authenticated services
    }

    override suspend fun syncServices() {
        // Implementation TODO: Complex sync logic
        // 1. Get primary list
        // 2. Get other lists
        // 3. Diff and update
    }

    private fun mapAniListStatus(status: String): UserAnimeStatus {
        return when (status) {
            "CURRENT", "REPEATING" -> UserAnimeStatus.WATCHING
            "PLANNING" -> UserAnimeStatus.PLAN_TO_WATCH
            "COMPLETED" -> UserAnimeStatus.COMPLETED
            "DROPPED" -> UserAnimeStatus.DROPPED
            "PAUSED" -> UserAnimeStatus.ON_HOLD
            else -> UserAnimeStatus.PLAN_TO_WATCH
        }
    }
}
