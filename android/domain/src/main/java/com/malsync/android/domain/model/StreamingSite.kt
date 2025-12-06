package com.malsync.android.domain.model

/**
 * Represents a supported streaming/reading site
 * Ported from src/pages/pages.ts
 */
data class StreamingSite(
    val name: String,
    val domain: String,
    val type: SiteType,
    val urlPatterns: List<Regex>,
    val iconUrl: String? = null,
    val supported: Boolean = true
)

enum class SiteType {
    ANIME,
    MANGA,
    BOTH
}

/**
 * Detected content from a URL
 */
data class DetectedContent(
    val site: StreamingSite,
    val url: String,
    val title: String? = null,
    val episodeOrChapter: Int? = null,
    val type: SiteType
)

/**
 * Bookmark for a streaming site
 */
data class Bookmark(
    val id: String,
    val animeId: String?,
    val mangaId: String?,
    val siteName: String,
    val url: String,
    val title: String,
    val lastEpisode: Int? = null,
    val lastChapter: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)
