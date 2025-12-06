package com.malsync.android.domain.repository

import com.malsync.android.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for anime data
 */
interface AnimeRepository {
    
    /**
     * Get anime list for a provider
     */
    fun getAnimeList(provider: SyncProvider, status: UserAnimeStatus? = null): Flow<List<Anime>>
    
    /**
     * Get single anime by ID
     */
    suspend fun getAnime(provider: SyncProvider, id: String): Result<Anime>
    
    /**
     * Search anime across providers
     */
    suspend fun searchAnime(query: String, providers: List<SyncProvider>): Result<List<Anime>>
    
    /**
     * Update anime progress
     */
    suspend fun updateAnimeProgress(
        provider: SyncProvider,
        animeId: String,
        episode: Int,
        status: UserAnimeStatus? = null,
        score: Int? = null
    ): Result<Anime>
    
    /**
     * Add anime to list
     */
    suspend fun addAnimeToList(
        provider: SyncProvider,
        animeId: String,
        status: UserAnimeStatus
    ): Result<Anime>
    
    /**
     * Remove anime from list
     */
    suspend fun removeAnimeFromList(provider: SyncProvider, animeId: String): Result<Unit>
    
    /**
     * Sync anime list from remote to local
     */
    suspend fun syncAnimeList(provider: SyncProvider): Result<Unit>
    
    /**
     * Get anime by URL (for content detection)
     */
    suspend fun getAnimeByUrl(url: String): Result<Anime?>
}
