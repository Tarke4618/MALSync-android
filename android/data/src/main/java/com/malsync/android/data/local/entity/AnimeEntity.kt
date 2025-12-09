package com.malsync.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malsync.android.domain.model.AnimeStatus
import com.malsync.android.domain.model.AnimeType
import com.malsync.android.domain.model.SyncProvider

@Entity(tableName = "anime_cache")
data class AnimeEntity(
    @PrimaryKey val id: String, // Composite ID or primary provider ID? Ideally one ID. For now assume UUID or primary provider ID.
    val title: String,
    val englishTitle: String?,
    val synonyms: List<String>,
    val type: AnimeType,
    val status: AnimeStatus,
    val synopsis: String?,
    val coverImage: String?,
    val bannerImage: String?,
    val episodes: Int?,
    val score: Float?,
    val season: String?,
    val year: Int?,
    val genres: List<String>,
    val studios: List<String>,
    val provider: SyncProvider,
    val providerIds: Map<SyncProvider, String>,
    val lastUpdated: Long
)
