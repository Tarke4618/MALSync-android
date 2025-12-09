package com.malsync.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_tokens")
data class AuthTokenEntity(
    @PrimaryKey
    val provider: String,
    val accessToken: String,
    val refreshToken: String?,
    val expiresAt: Long,
    val tokenType: String
)
