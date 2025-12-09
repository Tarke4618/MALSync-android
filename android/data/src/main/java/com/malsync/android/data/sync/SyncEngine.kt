package com.malsync.android.data.sync

import com.malsync.android.data.local.dao.AnimeDao
import com.malsync.android.data.local.dao.UserListEntryDao
import com.malsync.android.data.local.entity.UserListEntryEntity
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.model.UserAnimeStatus
import com.malsync.android.domain.repository.MappingRepository
import com.malsync.android.domain.repository.MultiServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncEngine @Inject constructor(
    private val repository: MultiServiceRepository,
    private val mappingRepository: MappingRepository,
    private val animeDao: AnimeDao,
    private val userListEntryDao: UserListEntryDao
) {

    suspend fun sync() = withContext(Dispatchers.IO) {
        // 1. Fetch Local Data
        val localEntries = userListEntryDao.getAllEntriesSync().associateBy { it.animeId }
        
        // 2. Fetch Remote Data from ALL enabled providers
        // For MVP, we iterate specific providers or assume we know which ones.
        // Let's genericize:
        val providers = SyncProvider.values()
        
        providers.forEach { provider ->
            try {
                syncProvider(provider, localEntries)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // 3. Push dirty local entries
        val dirtyEntries = userListEntryDao.getDirtyEntries()
        dirtyEntries.forEach { entry ->
            // TODO: Resolve which provider to push to, or push to all mapped
            // For now, simpler logic handled in syncProvider or separate push phase
        }
    }

    private suspend fun syncProvider(
        provider: SyncProvider,
        localEntries: Map<String, UserListEntryEntity>
    ) {
        val remoteList = repository.fetchUserAnimeList(provider)
        
        remoteList.forEach { remoteEntry ->
            // Map remote ID to unified ID (mapped ID)
            // Ideally remoteEntry.animeId IS the mapped ID if repository did mapping, 
            // but repository usually returns provider-specific ID unless mapped.
            // Assumption: toUserAnimeListEntry() in repo might not map ID.
            // We need to map it here.
            
            val unifiedId = resolveUnifiedId(provider, remoteEntry.animeId) ?: return@forEach
            
            val localEntry = localEntries[unifiedId]
            
            if (localEntry == null) {
                // New entry from remote -> Insert Local
                userListEntryDao.insertEntry(
                    UserListEntryEntity(
                        animeId = unifiedId,
                        title = remoteEntry.title,
                        imageUrl = remoteEntry.imageUrl,
                        status = remoteEntry.status,
                        score = remoteEntry.score,
                        watchedEpisodes = remoteEntry.watchedEpisodes,
                        totalEpisodes = remoteEntry.totalEpisodes,
                        updatedAt = remoteEntry.updatedAt
                    )
                )
            } else {
                // Conflict Resolution: Latest Modified Wins
                if (remoteEntry.updatedAt > localEntry.updatedAt) {
                    // Remote is newer -> Update Local
                    userListEntryDao.insertEntry(
                        localEntry.copy(
                            title = remoteEntry.title, // Update display info too
                            imageUrl = remoteEntry.imageUrl,
                            status = remoteEntry.status,
                            score = remoteEntry.score,
                            watchedEpisodes = remoteEntry.watchedEpisodes,
                            totalEpisodes = remoteEntry.totalEpisodes,
                            updatedAt = remoteEntry.updatedAt,
                            dirty = false
                        )
                    )
                } else if (localEntry.updatedAt > remoteEntry.updatedAt) {
                    // Local is newer -> Push to Remote
                    repository.updateUserAnimeStatus(
                        remoteEntry.copy(
                            status = localEntry.status,
                            score = localEntry.score,
                            watchedEpisodes = localEntry.watchedEpisodes,
                            updatedAt = localEntry.updatedAt
                        ),
                        provider
                    )
                }
            }
        }
    }

    private suspend fun resolveUnifiedId(provider: SyncProvider, remoteId: String): String? {
        // If provider is MAL, we use that as Unified ID for now (MVP simplification)
        if (provider == SyncProvider.MYANIMELIST) return remoteId
        
        // Otherwise, look up mapping
        // We need 'from provider ID' -> 'MAL ID'
        // MappingRepository.getRemoteId expects (fromProvider, fromId, toProvider)
        // If we want unified (MAL) ID:
        return mappingRepository.getRemoteId(provider, remoteId, SyncProvider.MYANIMELIST)
    }
}
