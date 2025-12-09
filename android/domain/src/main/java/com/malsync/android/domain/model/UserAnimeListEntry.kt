package com.malsync.android.domain.model

data class UserAnimeListEntry(
    val animeId: String, // Maps to Anime.id
    val title: String,
    val imageUrl: String?,
    val status: UserAnimeStatus,
    val score: Float, // Supports 0-100 or 0.0-10.0
    val watchedEpisodes: Int,
    val totalEpisodes: Int,
    val updatedAt: Long,
    val provider: SyncProvider? = null // Source of this entry
)
