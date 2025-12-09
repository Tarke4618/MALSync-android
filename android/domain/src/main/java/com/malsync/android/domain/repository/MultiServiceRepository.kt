package com.malsync.android.domain.repository

import com.malsync.android.domain.model.Anime
import com.malsync.android.domain.model.UserAnimeListEntry
import kotlinx.coroutines.flow.Flow

interface MultiServiceRepository {
    fun getAnimeDetails(id: String): Flow<Anime>
    fun searchAnime(query: String): Flow<List<Anime>>
    
    // User specific
    fun getUserAnimeList(): Flow<List<UserAnimeListEntry>>
    suspend fun fetchUserAnimeList(provider: SyncProvider): List<UserAnimeListEntry>
    suspend fun updateUserAnimeStatus(entry: UserAnimeListEntry)
    suspend fun updateUserAnimeStatus(entry: UserAnimeListEntry, provider: SyncProvider)
    
    // Sync
    suspend fun syncServices()
}
