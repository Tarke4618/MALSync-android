package com.malsync.android.data.repository

import com.malsync.android.data.local.dao.MappingDao
import com.malsync.android.data.local.entity.MappingEntity
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.repository.MappingRepository
import javax.inject.Inject

class MappingRepositoryImpl @Inject constructor(
    private val mappingDao: MappingDao
) : MappingRepository {

    override suspend fun getRemoteId(
        fromProvider: SyncProvider,
        fromId: String,
        toProvider: SyncProvider
    ): String? {
        // Current logic assumes we can look up by MAL ID. 
        // If fromProvider is NOT MAL, implementation is harder without extra indices or scanning.
        // For this MVP, we assume MAL is the anchor.
        
        val mapping = if (fromProvider == SyncProvider.MYANIMELIST) {
            mappingDao.getMappingByMalId(fromId)
        } else {
            // TODO: Add lookup by other provider IDs in DAO if needed.
            // For now return null or scan (inefficient).
            return null
        }
        
        return mapping?.let {
            when (toProvider) {
                SyncProvider.MYANIMELIST -> it.malId
                SyncProvider.ANILIST -> it.anilistId
                SyncProvider.KITSU -> it.kitsuId
                SyncProvider.SIMKL -> it.simklId
                SyncProvider.SHIKIMORI -> it.shikimoriId
            }
        }
    }

    override suspend fun saveMapping(malId: Int, otherAndId: Pair<SyncProvider, String>) {
         saveMapping(malId.toString(), otherAndId)
    }
    
    suspend fun saveMapping(malId: String, otherAndId: Pair<SyncProvider, String>) {
        val current = mappingDao.getMappingByMalId(malId)
        
        val newMapping = if (current != null) {
            when (otherAndId.first) {
                SyncProvider.MYANIMELIST -> current.copy(malId = otherAndId.second)
                SyncProvider.ANILIST -> current.copy(anilistId = otherAndId.second)
                SyncProvider.KITSU -> current.copy(kitsuId = otherAndId.second)
                SyncProvider.SIMKL -> current.copy(simklId = otherAndId.second)
                SyncProvider.SHIKIMORI -> current.copy(shikimoriId = otherAndId.second)
            }
        } else {
            MappingEntity(
                malId = malId,
                anilistId = if (otherAndId.first == SyncProvider.ANILIST) otherAndId.second else null,
                kitsuId = if (otherAndId.first == SyncProvider.KITSU) otherAndId.second else null,
                simklId = if (otherAndId.first == SyncProvider.SIMKL) otherAndId.second else null,
                shikimoriId = if (otherAndId.first == SyncProvider.SHIKIMORI) otherAndId.second else null
            )
        }
        
        mappingDao.insertMapping(newMapping)
    }
}
