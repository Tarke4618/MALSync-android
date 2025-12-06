package com.malsync.android.data.repository

import com.malsync.android.domain.model.DetectedContent
import com.malsync.android.domain.model.SiteType
import com.malsync.android.domain.model.StreamingSite
import com.malsync.android.domain.repository.StreamingSiteRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of StreamingSiteRepository
 * Provides hardcoded list of supported streaming sites and URL pattern matching
 */
@Singleton
class StreamingSiteRepositoryImpl @Inject constructor() : StreamingSiteRepository {

    private val supportedSites = listOf(
        // Crunchyroll
        StreamingSite(
            name = "Crunchyroll",
            domain = "crunchyroll.com",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?crunchyroll\\.com/([^/]+)/episode-([0-9]+).*"),
                Regex("https?://(?:www\\.)?crunchyroll\\.com/watch/([A-Z0-9]+)/(.*)")
            )
        ),
        
        // 9anime
        StreamingSite(
            name = "9anime",
            domain = "9anime.to",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?9anime\\.[^/]+/watch/([^/]+).*"),
                Regex("https?://(?:www\\.)?9anime\\.[^/]+/([^/]+)-episode-([0-9]+)")
            )
        ),
        
        // Gogoanime
        StreamingSite(
            name = "Gogoanime",
            domain = "gogoanime.com",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:[^/]*\\.)?gogo-?anime\\.[^/]+/([^/]+)-episode-([0-9]+)"),
                Regex("https?://(?:[^/]*\\.)?gogo-?anime\\.[^/]+/category/([^/]+)")
            )
        ),
        
        // Zoro.to
        StreamingSite(
            name = "Zoro",
            domain = "zoro.to",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?zoro\\.to/watch/([^?]+)\\?ep=([0-9]+)"),
                Regex("https?://(?:www\\.)?zoro\\.to/([^/?]+)")
            )
        ),
        
        // AnimeFLV
        StreamingSite(
            name = "AnimeFLV",
            domain = "animeflv.net",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?animeflv\\.net/ver/([^/]+)-([0-9]+)"),
                Regex("https?://(?:www\\.)?animeflv\\.net/anime/([^/]+)")
            )
        ),
        
        // Anime-Planet
        StreamingSite(
            name = "Anime-Planet",
            domain = "anime-planet.com",
            type = SiteType.BOTH,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?anime-planet\\.com/anime/([^/]+)/videos/([0-9]+)"),
                Regex("https?://(?:www\\.)?anime-planet\\.com/anime/([^/]+)"),
                Regex("https?://(?:www\\.)?anime-planet\\.com/manga/([^/]+)")
            )
        ),
        
        // Aniwatch (formerly AniMixPlay)
        StreamingSite(
            name = "Aniwatch",
            domain = "aniwatch.to",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?aniwatch\\.to/watch/([^?]+)\\?ep=([0-9]+)"),
                Regex("https?://(?:www\\.)?aniwatch\\.to/([^/?]+)")
            )
        ),
        
        // KissAnime (various domains)
        StreamingSite(
            name = "KissAnime",
            domain = "kissanime.com.ru",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:[^/]*\\.)?kissanime\\.[^/]+/anime/([^/]+)/episode-([0-9]+)"),
                Regex("https?://(?:[^/]*\\.)?kissanime\\.[^/]+/anime/([^/]+)")
            )
        ),
        
        // MyAnimeList (not for streaming but for linking)
        StreamingSite(
            name = "MyAnimeList",
            domain = "myanimelist.net",
            type = SiteType.BOTH,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?myanimelist\\.net/anime/([0-9]+)/?.*"),
                Regex("https?://(?:www\\.)?myanimelist\\.net/manga/([0-9]+)/?.*")
            )
        ),
        
        // AniList
        StreamingSite(
            name = "AniList",
            domain = "anilist.co",
            type = SiteType.BOTH,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?anilist\\.co/anime/([0-9]+)/?.*"),
                Regex("https?://(?:www\\.)?anilist\\.co/manga/([0-9]+)/?.*")
            )
        ),
        
        // AnimePahe
        StreamingSite(
            name = "AnimePahe",
            domain = "animepahe.com",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?animepahe\\.com/play/([^/]+)/([a-f0-9]+)"),
                Regex("https?://(?:www\\.)?animepahe\\.com/anime/([^/]+)")
            )
        ),
        
        // Twist.moe
        StreamingSite(
            name = "Twist",
            domain = "twist.moe",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?twist\\.moe/a/([^/]+)/([0-9]+)"),
                Regex("https?://(?:www\\.)?twist\\.moe/a/([^/]+)")
            )
        ),
        
        // MangaDex
        StreamingSite(
            name = "MangaDex",
            domain = "mangadex.org",
            type = SiteType.MANGA,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?mangadex\\.org/title/([a-f0-9-]+)/?.*"),
                Regex("https?://(?:www\\.)?mangadex\\.org/chapter/([a-f0-9-]+)/?.*")
            )
        ),
        
        // MangaPlus (Shueisha official)
        StreamingSite(
            name = "MangaPlus",
            domain = "mangaplus.shueisha.co.jp",
            type = SiteType.MANGA,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?mangaplus\\.shueisha\\.co\\.jp/titles/([0-9]+)"),
                Regex("https?://(?:www\\.)?mangaplus\\.shueisha\\.co\\.jp/viewer/([0-9]+)")
            )
        ),
        
        // HiAnime (formerly Aniwatch)
        StreamingSite(
            name = "HiAnime",
            domain = "hianime.to",
            type = SiteType.ANIME,
            urlPatterns = listOf(
                Regex("https?://(?:www\\.)?hianime\\.to/watch/([^?]+)\\?ep=([0-9]+)"),
                Regex("https?://(?:www\\.)?hianime\\.to/([^/?]+)")
            )
        )
    )

    override suspend fun getSupportedSites(): List<StreamingSite> {
        return supportedSites
    }

    override suspend fun detectContent(url: String): DetectedContent? {
        for (site in supportedSites) {
            for (pattern in site.urlPatterns) {
                val match = pattern.find(url)
                if (match != null) {
                    // Try to extract episode/chapter number from the match
                    val episodeOrChapter = extractEpisodeNumber(match)
                    
                    return DetectedContent(
                        site = site,
                        url = url,
                        title = extractTitle(match),
                        episodeOrChapter = episodeOrChapter,
                        type = site.type
                    )
                }
            }
        }
        return null
    }

    override suspend fun getInjectionScript(url: String): String? {
        // For MVP, we don't have injection scripts yet
        // In future, this would return JavaScript to inject into the WebView
        // to detect anime/manga information from the page
        return null
    }

    override suspend fun isSiteSupported(url: String): Boolean {
        return detectContent(url) != null
    }

    override suspend fun getSiteByDomain(domain: String): StreamingSite? {
        return supportedSites.find { it.domain.contains(domain, ignoreCase = true) }
    }

    /**
     * Extract episode or chapter number from regex match
     */
    private fun extractEpisodeNumber(match: MatchResult): Int? {
        // Try to find a numeric group in the match
        // Usually the last numeric group is the episode/chapter number
        for (i in match.groupValues.size - 1 downTo 1) {
            val value = match.groupValues[i]
            val number = value.toIntOrNull()
            if (number != null && number > 0) {
                return number
            }
        }
        return null
    }

    /**
     * Extract title from regex match
     */
    private fun extractTitle(match: MatchResult): String? {
        // Usually the first group is the title/slug
        return match.groupValues.getOrNull(1)?.let { title ->
            // Clean up the title by replacing dashes/underscores with spaces
            title.replace(Regex("[-_]"), " ")
                .split(" ")
                .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
        }
    }
}
