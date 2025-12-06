package com.malsync.android.data.mapper

import com.malsync.android.data.remote.dto.mal.*
import com.malsync.android.domain.model.*

/**
 * Mappers to convert MAL DTOs to domain models
 */

fun MalAnimeResponse.toDomain(provider: SyncProvider = SyncProvider.MYANIMELIST): Anime {
    return Anime(
        id = id.toString(),
        title = title,
        englishTitle = alternativeTitles?.en,
        synonyms = alternativeTitles?.synonyms.orEmpty(),
        type = mediaType.toAnimeType(),
        status = status.toAnimeStatus(),
        synopsis = synopsis,
        coverImage = mainPicture?.large ?: mainPicture?.medium,
        bannerImage = null,
        episodes = numEpisodes,
        currentEpisode = myListStatus?.numEpisodesWatched ?: 0,
        score = mean,
        userScore = myListStatus?.score,
        userStatus = myListStatus?.status?.toUserAnimeStatus(),
        startDate = startDate,
        endDate = endDate,
        season = startSeason?.season,
        year = startSeason?.year,
        genres = genres?.map { it.name }.orEmpty(),
        studios = studios?.map { it.name }.orEmpty(),
        provider = provider,
        providerIds = mapOf(provider to id.toString()),
        streamingLinks = emptyList(),
        nextEpisodeAiring = null
    )
}

fun MalAnimeListItem.toDomain(): Anime {
    return node.toDomain(SyncProvider.MYANIMELIST).copy(
        currentEpisode = listStatus?.numEpisodesWatched ?: 0,
        userScore = listStatus?.score,
        userStatus = listStatus?.status?.toUserAnimeStatus()
    )
}

private fun String?.toAnimeType(): AnimeType {
    return when (this?.lowercase()) {
        "tv" -> AnimeType.TV
        "movie" -> AnimeType.MOVIE
        "ova" -> AnimeType.OVA
        "ona" -> AnimeType.ONA
        "special" -> AnimeType.SPECIAL
        else -> AnimeType.UNKNOWN
    }
}

private fun String?.toAnimeStatus(): AnimeStatus {
    return when (this?.lowercase()) {
        "currently_airing", "airing" -> AnimeStatus.AIRING
        "finished_airing", "finished" -> AnimeStatus.FINISHED
        "not_yet_aired" -> AnimeStatus.NOT_YET_AIRED
        else -> AnimeStatus.UNKNOWN
    }
}

private fun String?.toUserAnimeStatus(): UserAnimeStatus? {
    return when (this?.lowercase()) {
        "watching" -> UserAnimeStatus.WATCHING
        "completed" -> UserAnimeStatus.COMPLETED
        "on_hold" -> UserAnimeStatus.ON_HOLD
        "dropped" -> UserAnimeStatus.DROPPED
        "plan_to_watch" -> UserAnimeStatus.PLAN_TO_WATCH
        else -> null
    }
}

fun UserAnimeStatus.toMalStatus(): String {
    return when (this) {
        UserAnimeStatus.WATCHING -> "watching"
        UserAnimeStatus.COMPLETED -> "completed"
        UserAnimeStatus.ON_HOLD -> "on_hold"
        UserAnimeStatus.DROPPED -> "dropped"
        UserAnimeStatus.PLAN_TO_WATCH -> "plan_to_watch"
    }
}

fun MalMangaResponse.toDomain(provider: SyncProvider = SyncProvider.MYANIMELIST): Manga {
    return Manga(
        id = id.toString(),
        title = title,
        englishTitle = alternativeTitles?.en,
        synonyms = alternativeTitles?.synonyms.orEmpty(),
        type = mediaType?.toMangaType() ?: MangaType.UNKNOWN,
        status = status?.toMangaStatus() ?: MangaStatus.UNKNOWN,
        synopsis = synopsis,
        coverImage = mainPicture?.large ?: mainPicture?.medium,
        chapters = numChapters,
        volumes = numVolumes,
        currentChapter = myListStatus?.numChaptersRead ?: 0,
        currentVolume = myListStatus?.numVolumesRead ?: 0,
        score = mean,
        userScore = myListStatus?.score,
        userStatus = myListStatus?.status?.toUserMangaStatus(),
        startDate = startDate,
        endDate = endDate,
        genres = genres?.map { it.name }.orEmpty(),
        provider = provider,
        providerIds = mapOf(provider to id.toString())
    )
}

private fun String.toMangaType(): MangaType {
    return when (this.lowercase()) {
        "manga" -> MangaType.MANGA
        "novel" -> MangaType.NOVEL
        "one_shot" -> MangaType.ONE_SHOT
        "doujinshi" -> MangaType.DOUJINSHI
        "manhwa" -> MangaType.MANHWA
        "manhua" -> MangaType.MANHUA
        else -> MangaType.UNKNOWN
    }
}

private fun String.toMangaStatus(): MangaStatus {
    return when (this.lowercase()) {
        "currently_publishing", "publishing" -> MangaStatus.PUBLISHING
        "finished" -> MangaStatus.FINISHED
        "not_yet_published" -> MangaStatus.NOT_YET_PUBLISHED
        "on_hiatus" -> MangaStatus.ON_HIATUS
        else -> MangaStatus.UNKNOWN
    }
}

private fun String.toUserMangaStatus(): UserMangaStatus? {
    return when (this.lowercase()) {
        "reading" -> UserMangaStatus.READING
        "completed" -> UserMangaStatus.COMPLETED
        "on_hold" -> UserMangaStatus.ON_HOLD
        "dropped" -> UserMangaStatus.DROPPED
        "plan_to_read" -> UserMangaStatus.PLAN_TO_READ
        else -> null
    }
}
