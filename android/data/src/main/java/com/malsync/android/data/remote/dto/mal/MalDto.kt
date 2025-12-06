package com.malsync.android.data.remote.dto.mal

import com.google.gson.annotations.SerializedName

/**
 * MAL API Response DTOs
 */

data class MalAnimeListResponse(
    @SerializedName("data")
    val data: List<MalAnimeListItem>,
    @SerializedName("paging")
    val paging: Paging
)

data class MalAnimeListItem(
    @SerializedName("node")
    val node: MalAnimeResponse,
    @SerializedName("list_status")
    val listStatus: MalListStatus?
)

data class MalAnimeResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("main_picture")
    val mainPicture: MalPicture?,
    @SerializedName("alternative_titles")
    val alternativeTitles: MalAlternativeTitles?,
    @SerializedName("start_date")
    val startDate: String?,
    @SerializedName("end_date")
    val endDate: String?,
    @SerializedName("synopsis")
    val synopsis: String?,
    @SerializedName("mean")
    val mean: Float?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("popularity")
    val popularity: Int?,
    @SerializedName("num_list_users")
    val numListUsers: Int?,
    @SerializedName("num_scoring_users")
    val numScoringUsers: Int?,
    @SerializedName("nsfw")
    val nsfw: String?,
    @SerializedName("media_type")
    val mediaType: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("genres")
    val genres: List<MalGenre>?,
    @SerializedName("my_list_status")
    val myListStatus: MalListStatus?,
    @SerializedName("num_episodes")
    val numEpisodes: Int?,
    @SerializedName("start_season")
    val startSeason: MalSeason?,
    @SerializedName("broadcast")
    val broadcast: MalBroadcast?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("average_episode_duration")
    val averageEpisodeDuration: Int?,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("studios")
    val studios: List<MalStudio>?
)

data class MalListStatus(
    @SerializedName("status")
    val status: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("num_episodes_watched")
    val numEpisodesWatched: Int,
    @SerializedName("is_rewatching")
    val isRewatching: Boolean,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class MalPicture(
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("large")
    val large: String?
)

data class MalAlternativeTitles(
    @SerializedName("synonyms")
    val synonyms: List<String>?,
    @SerializedName("en")
    val en: String?,
    @SerializedName("ja")
    val ja: String?
)

data class MalGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class MalSeason(
    @SerializedName("year")
    val year: Int,
    @SerializedName("season")
    val season: String
)

data class MalBroadcast(
    @SerializedName("day_of_the_week")
    val dayOfTheWeek: String?,
    @SerializedName("start_time")
    val startTime: String?
)

data class MalStudio(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class MalAnimeSearchResponse(
    @SerializedName("data")
    val data: List<MalAnimeSearchItem>,
    @SerializedName("paging")
    val paging: Paging
)

data class MalAnimeSearchItem(
    @SerializedName("node")
    val node: MalAnimeResponse
)

data class MalListStatusResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("num_episodes_watched")
    val numEpisodesWatched: Int,
    @SerializedName("is_rewatching")
    val isRewatching: Boolean,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class MalMangaListResponse(
    @SerializedName("data")
    val data: List<MalMangaListItem>,
    @SerializedName("paging")
    val paging: Paging
)

data class MalMangaListItem(
    @SerializedName("node")
    val node: MalMangaResponse,
    @SerializedName("list_status")
    val listStatus: MalMangaListStatus?
)

data class MalMangaResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("main_picture")
    val mainPicture: MalPicture?,
    @SerializedName("alternative_titles")
    val alternativeTitles: MalAlternativeTitles?,
    @SerializedName("start_date")
    val startDate: String?,
    @SerializedName("end_date")
    val endDate: String?,
    @SerializedName("synopsis")
    val synopsis: String?,
    @SerializedName("mean")
    val mean: Float?,
    @SerializedName("media_type")
    val mediaType: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("genres")
    val genres: List<MalGenre>?,
    @SerializedName("my_list_status")
    val myListStatus: MalMangaListStatus?,
    @SerializedName("num_volumes")
    val numVolumes: Int?,
    @SerializedName("num_chapters")
    val numChapters: Int?
)

data class MalMangaListStatus(
    @SerializedName("status")
    val status: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("num_volumes_read")
    val numVolumesRead: Int,
    @SerializedName("num_chapters_read")
    val numChaptersRead: Int,
    @SerializedName("is_rereading")
    val isRereading: Boolean,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class Paging(
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("next")
    val next: String?
)

data class MalUserProfile(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("joined_at")
    val joinedAt: String?,
    @SerializedName("anime_statistics")
    val animeStatistics: MalAnimeStatistics?,
    @SerializedName("time_zone")
    val timeZone: String?,
    @SerializedName("is_supporter")
    val isSupporter: Boolean?
)

data class MalAnimeStatistics(
    @SerializedName("num_items_watching")
    val numItemsWatching: Int?,
    @SerializedName("num_items_completed")
    val numItemsCompleted: Int?,
    @SerializedName("num_items_on_hold")
    val numItemsOnHold: Int?,
    @SerializedName("num_items_dropped")
    val numItemsDropped: Int?,
    @SerializedName("num_items_plan_to_watch")
    val numItemsPlanToWatch: Int?,
    @SerializedName("num_items")
    val numItems: Int?,
    @SerializedName("num_days_watched")
    val numDaysWatched: Float?,
    @SerializedName("num_days_watching")
    val numDaysWatching: Float?,
    @SerializedName("num_days_completed")
    val numDaysCompleted: Float?,
    @SerializedName("num_days_on_hold")
    val numDaysOnHold: Float?,
    @SerializedName("num_days_dropped")
    val numDaysDropped: Float?,
    @SerializedName("num_days")
    val numDays: Float?,
    @SerializedName("num_episodes")
    val numEpisodes: Int?,
    @SerializedName("num_times_rewatched")
    val numTimesRewatched: Int?,
    @SerializedName("mean_score")
    val meanScore: Float?
)
