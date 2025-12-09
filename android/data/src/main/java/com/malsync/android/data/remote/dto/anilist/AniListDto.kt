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
data class AniListMediaListCollectionResponse(
    @SerializedName("data")
    val data: AniListMediaListData
)

data class AniListMediaListData(
    @SerializedName("MediaListCollection")
    val mediaListCollection: AniListMediaListCollection
)

data class AniListMediaListCollection(
    @SerializedName("lists")
    val lists: List<AniListMediaListGroup>
)

data class AniListMediaListGroup(
    @SerializedName("entries")
    val entries: List<AniListMediaListEntry>
)

data class AniListMediaListEntry(
    @SerializedName("id")
    val id: Int,
    @SerializedName("mediaId")
    val mediaId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("score")
    val score: Float,
    @SerializedName("progress")
    val progress: Int,
    @SerializedName("repeat")
    val repeat: Int?,
    @SerializedName("updatedAt")
    val updatedAt: Long?,
    @SerializedName("media")
    val media: AniListMedia?
)

data class AniListMedia(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: AniListTitle?,
    @SerializedName("coverImage")
    val coverImage: AniListCoverImage?,
    @SerializedName("episodes")
    val episodes: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("averageScore")
    val averageScore: Int?
)

data class AniListTitle(
    @SerializedName("romaji")
    val romaji: String?,
    @SerializedName("english")
    val english: String?,
    @SerializedName("native")
    val native: String?
)

data class AniListCoverImage(
    @SerializedName("large")
    val large: String?,
    @SerializedName("medium")
    val medium: String?
)
