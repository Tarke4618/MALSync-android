package com.malsync.android.domain.model

/**
 * Unified anime model across all providers (MAL, AniList, Kitsu, Simkl)
 */
data class Anime(
    val id: String,
    val title: String,
    val englishTitle: String? = null,
    val synonyms: List<String> = emptyList(),
    val type: AnimeType,
    val status: AnimeStatus,
    val synopsis: String? = null,
    val coverImage: String? = null,
    val bannerImage: String? = null,
    val episodes: Int? = null,
    val currentEpisode: Int = 0,
    val score: Float? = null,
    val userScore: Int? = null,
    val userStatus: UserAnimeStatus? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val season: String? = null,
    val year: Int? = null,
    val genres: List<String> = emptyList(),
    val studios: List<String> = emptyList(),
    val provider: SyncProvider,
    val providerIds: Map<SyncProvider, String> = emptyMap(),
    val streamingLinks: List<StreamingLink> = emptyList(),
    val nextEpisodeAiring: Long? = null, // Unix timestamp
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class AnimeType {
    TV,
    MOVIE,
    OVA,
    ONA,
    SPECIAL,
    UNKNOWN
}

enum class AnimeStatus {
    AIRING,
    FINISHED,
    NOT_YET_AIRED,
    CANCELLED,
    UNKNOWN
}

enum class UserAnimeStatus {
    WATCHING,
    COMPLETED,
    ON_HOLD,
    DROPPED,
    PLAN_TO_WATCH
}

data class StreamingLink(
    val siteName: String,
    val url: String,
    val episode: Int? = null
)
