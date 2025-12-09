package com.malsync.android.domain.model

/**
 * Supported sync providers
 */
enum class SyncProvider {
    MYANIMELIST,
    ANILIST,
    KITSU,
    SIMKL,
    SHIKIMORI;

    val displayName: String
        get() = when (this) {
            MYANIMELIST -> "MyAnimeList"
            ANILIST -> "AniList"
            KITSU -> "Kitsu"
            SIMKL -> "Simkl"
            SHIKIMORI -> "Shikimori"
        }

    val oauthUrl: String
        get() = when (this) {
            MYANIMELIST -> "https://myanimelist.net/v1/oauth2/authorize"
            ANILIST -> "https://anilist.co/api/v2/oauth/authorize"
            KITSU -> "https://kitsu.io/api/oauth/authorize"
            SIMKL -> "https://simkl.com/oauth/authorize"
            SHIKIMORI -> "https://shikimori.one/oauth/authorize"
        }

    val apiBaseUrl: String
        get() = when (this) {
            MYANIMELIST -> "https://api.myanimelist.net/v2"
            ANILIST -> "https://graphql.anilist.co"
            KITSU -> "https://kitsu.io/api"
            SIMKL -> "https://api.simkl.com"
            SHIKIMORI -> "https://shikimori.one/api"
        }
}

/**
 * Authentication token for a provider
 */
data class AuthToken(
    val provider: SyncProvider,
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresAt: Long, // Unix timestamp
    val tokenType: String = "Bearer"
) {
    fun isExpired(): Boolean {
        return System.currentTimeMillis() > expiresAt
    }
}

/**
 * User profile for a provider
 */
data class UserProfile(
    val provider: SyncProvider,
    val userId: String,
    val username: String,
    val avatarUrl: String? = null,
    val animeCount: Int = 0,
    val mangaCount: Int = 0
)
