package com.malsync.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.malsync.android.data.local.converter.Converters

@Entity(tableName = "manga")
@TypeConverters(Converters::class)
data class MangaEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val englishTitle: String?,
    val synonyms: List<String>,
    val type: String,
    val status: String,
    val synopsis: String?,
    val coverImage: String?,
    val chapters: Int?,
    val volumes: Int?,
    val currentChapter: Int,
    val currentVolume: Int,
    val score: Float?,
    val userScore: Int?,
    val userStatus: String?,
    val startDate: String?,
    val endDate: String?,
    val genres: List<String>,
    val authors: List<String>,
    val provider: String,
    val providerIds: Map<String, String>,
    val lastUpdated: Long
)
