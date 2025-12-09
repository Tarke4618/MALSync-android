package com.malsync.android.domain.repository

import com.malsync.android.domain.model.SyncProvider

interface MappingRepository {
    suspend fun getRemoteId(fromProvider: SyncProvider, fromId: String, toProvider: SyncProvider): String?
    suspend fun saveMapping(malId: Int, otherAndId: Pair<SyncProvider, String>)
}
