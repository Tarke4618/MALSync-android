package com.malsync.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malsync.android.domain.model.UserAnimeStatus

@Entity(tableName = "user_list")
data class UserListEntryEntity(
    @PrimaryKey val animeId: String,
    val title: String,
    val imageUrl: String?,
    val status: UserAnimeStatus,
    val score: Float,
    val watchedEpisodes: Int,
    val totalEpisodes: Int,
    val updatedAt: Long,
    val dirty: Boolean = false // Flag to indicate if local change needs to be pushed
)
