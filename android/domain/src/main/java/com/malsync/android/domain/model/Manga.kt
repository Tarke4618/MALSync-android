package com.malsync.android.domain.model

/**
 * Unified manga model across all providers
 */
data class Manga(
    val id: String,
    val title: String,
    val englishTitle: String? = null,
    val synonyms: List<String> = emptyList(),
    val type: MangaType,
    val status: MangaStatus,
    val synopsis: String? = null,
    val coverImage: String? = null,
    val chapters: Int? = null,
    val volumes: Int? = null,
    val currentChapter: Int = 0,
    val currentVolume: Int = 0,
    val score: Float? = null,
    val userScore: Int? = null,
    val userStatus: UserMangaStatus? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val genres: List<String> = emptyList(),
    val authors: List<String> = emptyList(),
    val provider: SyncProvider,
    val providerIds: Map<SyncProvider, String> = emptyMap(),
    val readingLinks: List<ReadingLink> = emptyList(),
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class MangaType {
    MANGA,
    MANHWA,
    MANHUA,
    LIGHT_NOVEL,
    NOVEL,
    ONE_SHOT,
    DOUJINSHI,
    UNKNOWN
}

enum class MangaStatus {
    PUBLISHING,
    FINISHED,
    NOT_YET_PUBLISHED,
    CANCELLED,
    ON_HIATUS,
    UNKNOWN
}

enum class UserMangaStatus {
    READING,
    COMPLETED,
    ON_HOLD,
    DROPPED,
    PLAN_TO_READ
}

data class ReadingLink(
    val siteName: String,
    val url: String,
    val chapter: Int? = null
)
