package com.malsync.android.data.remote.dto.anilist

import com.google.gson.annotations.SerializedName

/**
 * AniList GraphQL API Response DTOs
 */

data class AniListUserProfileResponse(
    @SerializedName("data")
    val data: AniListViewerData
)

data class AniListViewerData(
    @SerializedName("Viewer")
    val viewer: AniListViewer
)

data class AniListViewer(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("avatar")
    val avatar: AniListAvatar?,
    @SerializedName("statistics")
    val statistics: AniListStatistics?
)

data class AniListAvatar(
    @SerializedName("large")
    val large: String?,
    @SerializedName("medium")
    val medium: String?
)

data class AniListStatistics(
    @SerializedName("anime")
    val anime: AniListAnimeStatistics?,
    @SerializedName("manga")
    val manga: AniListMangaStatistics?
)

data class AniListAnimeStatistics(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("meanScore")
    val meanScore: Float?,
    @SerializedName("minutesWatched")
    val minutesWatched: Int?
)

data class AniListMangaStatistics(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("meanScore")
    val meanScore: Float?,
    @SerializedName("chaptersRead")
    val chaptersRead: Int?
)
