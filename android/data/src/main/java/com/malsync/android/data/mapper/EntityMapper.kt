package com.malsync.android.data.mapper

import com.malsync.android.data.local.entity.*
import com.malsync.android.domain.model.*

/**
 * Mappers between domain models and database entities
 */

fun Anime.toEntity(): AnimeEntity {
    return AnimeEntity(
        id = id,
        title = title,
        englishTitle = englishTitle,
        synonyms = synonyms,
        type = type,
        status = status,
        synopsis = synopsis,
        coverImage = coverImage,
        bannerImage = bannerImage,
        episodes = episodes,
        currentEpisode = currentEpisode,
        score = score,
        userScore = userScore,
        userStatus = userStatus,
        startDate = startDate,
        endDate = endDate,
        season = season,
        year = year,
        genres = genres,
        studios = studios,
        provider = provider,
        providerIds = providerIds,
        nextEpisodeAiring = nextEpisodeAiring,
        lastUpdated = lastUpdated
    )
}

fun AnimeEntity.toDomain(): Anime {
    return Anime(
        id = id,
        title = title,
        englishTitle = englishTitle,
        synonyms = synonyms,
        type = type,
        status = status,
        synopsis = synopsis,
        coverImage = coverImage,
        bannerImage = bannerImage,
        episodes = episodes,
        currentEpisode = currentEpisode,
        score = score,
        userScore = userScore,
        userStatus = userStatus,
        startDate = startDate,
        endDate = endDate,
        season = season,
        year = year,
        genres = genres,
        studios = studios,
        provider = provider,
        providerIds = providerIds,
        streamingLinks = emptyList(),
        nextEpisodeAiring = nextEpisodeAiring,
        lastUpdated = lastUpdated
    )
}

fun Manga.toEntity(): MangaEntity {
    return MangaEntity(
        id = id,
        title = title,
        englishTitle = englishTitle,
        synonyms = synonyms,
        type = type.name,
        status = status.name,
        synopsis = synopsis,
        coverImage = coverImage,
        chapters = chapters,
        volumes = volumes,
        currentChapter = currentChapter,
        currentVolume = currentVolume,
        score = score,
        userScore = userScore,
        userStatus = userStatus?.name,
        startDate = startDate,
        endDate = endDate,
        genres = genres,
        authors = authors,
        provider = provider.name,
        providerIds = providerIds.mapKeys { it.key.name },
        lastUpdated = lastUpdated
    )
}

fun MangaEntity.toDomain(): Manga {
    return Manga(
        id = id,
        title = title,
        englishTitle = englishTitle,
        synonyms = synonyms,
        type = MangaType.valueOf(type),
        status = MangaStatus.valueOf(status),
        synopsis = synopsis,
        coverImage = coverImage,
        chapters = chapters,
        volumes = volumes,
        currentChapter = currentChapter,
        currentVolume = currentVolume,
        score = score,
        userScore = userScore,
        userStatus = userStatus?.let { UserMangaStatus.valueOf(it) },
        startDate = startDate,
        endDate = endDate,
        genres = genres,
        authors = authors,
        provider = SyncProvider.valueOf(provider),
        providerIds = providerIds.mapKeys { SyncProvider.valueOf(it.key) },
        readingLinks = emptyList(),
        lastUpdated = lastUpdated
    )
}

fun AuthToken.toEntity(): AuthTokenEntity {
    return AuthTokenEntity(
        provider = provider.name,
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresAt = expiresAt,
        tokenType = tokenType
    )
}

fun AuthTokenEntity.toDomain(): AuthToken {
    return AuthToken(
        provider = SyncProvider.valueOf(provider),
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresAt = expiresAt,
        tokenType = tokenType
    )
}

fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        provider = provider.name,
        userId = userId,
        username = username,
        avatarUrl = avatarUrl,
        animeCount = animeCount,
        mangaCount = mangaCount
    )
}

fun UserProfileEntity.toDomain(): UserProfile {
    return UserProfile(
        provider = SyncProvider.valueOf(provider),
        userId = userId,
        username = username,
        avatarUrl = avatarUrl,
        animeCount = animeCount,
        mangaCount = mangaCount
    )
}
