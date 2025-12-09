package com.malsync.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey
    val provider: String,
    val userId: String,
    val username: String,
    val avatarUrl: String?,
    val animeCount: Int,
    val mangaCount: Int
)
