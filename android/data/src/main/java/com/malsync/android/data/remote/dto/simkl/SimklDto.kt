package com.malsync.android.data.remote.dto.simkl

import com.google.gson.annotations.SerializedName

/**
 * Simkl API DTOs
 */

data class SimklAnimeListResponse(
    @SerializedName("anime")
    val anime: List<SimklAnimeListItem>
)

data class SimklAnimeListItem(
    @SerializedName("last_watched_at")
    val lastWatchedAt: String?,
    @SerializedName("status")
    val status: String,
    @SerializedName("user_rating")
    val userRating: Int?,
    @SerializedName("last_watched")
    val lastWatched: String?,
    @SerializedName("watched_episodes_count")
    val watchedEpisodesCount: Int?,
    @SerializedName("total_episodes_count")
    val totalEpisodesCount: Int?,
    @SerializedName("show")
    val show: SimklAnimeResponse
)

data class SimklAnimeResponse(
    @SerializedName("title")
    val title: String,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("ids")
    val ids: SimklIds,
    @SerializedName("poster")
    val poster: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("total_episodes")
    val totalEpisodes: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("anime_type")
    val animeType: String?,
    @SerializedName("en_title")
    val enTitle: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("genres")
    val genres: List<String>?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("ratings")
    val ratings: SimklRatings?,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("certification")
    val certification: String?
)

data class SimklIds(
    @SerializedName("simkl")
    val simkl: Int,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("mal")
    val mal: String?,
    @SerializedName("anilist")
    val anilist: String?,
    @SerializedName("anidb")
    val anidb: String?,
    @SerializedName("animeplanet")
    val animePlanet: String?,
    @SerializedName("kitsu")
    val kitsu: String?,
    @SerializedName("livechart")
    val liveChart: String?
)

data class SimklRatings(
    @SerializedName("simkl")
    val simkl: SimklRating?,
    @SerializedName("mal")
    val mal: SimklRating?
)

data class SimklRating(
    @SerializedName("rating")
    val rating: Float?,
    @SerializedName("votes")
    val votes: Int?
)

data class SimklAnimeSearchResult(
    @SerializedName("title")
    val title: String,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("ids")
    val ids: SimklIds,
    @SerializedName("poster")
    val poster: String?,
    @SerializedName("type")
    val type: String?
)

data class SimklHistoryRequest(
    @SerializedName("shows")
    val shows: List<SimklHistoryShow>
)

data class SimklHistoryShow(
    @SerializedName("title")
    val title: String?,
    @SerializedName("ids")
    val ids: SimklShowIds,
    @SerializedName("episodes")
    val episodes: List<SimklEpisode>
)

data class SimklShowIds(
    @SerializedName("simkl")
    val simkl: Int?,
    @SerializedName("mal")
    val mal: String?
)

data class SimklEpisode(
    @SerializedName("number")
    val number: Int,
    @SerializedName("watched_at")
    val watchedAt: String? = null
)

data class SimklAddToListRequest(
    @SerializedName("shows")
    val shows: List<SimklShowToAdd>
)

data class SimklShowToAdd(
    @SerializedName("ids")
    val ids: SimklShowIds,
    @SerializedName("to")
    val to: String // watching, plantowatch, hold, completed, dropped
)

data class SimklRemoveFromListRequest(
    @SerializedName("shows")
    val shows: List<SimklShowToRemove>
)

data class SimklShowToRemove(
    @SerializedName("ids")
    val ids: SimklShowIds
)

data class SimklSyncResponse(
    @SerializedName("added")
    val added: SimklSyncStats?,
    @SerializedName("not_found")
    val notFound: SimklSyncStats?,
    @SerializedName("deleted")
    val deleted: SimklSyncStats?
)

data class SimklSyncStats(
    @SerializedName("shows")
    val shows: Int?,
    @SerializedName("episodes")
    val episodes: Int?,
    @SerializedName("movies")
    val movies: Int?
)

data class SimklUserResponse(
    @SerializedName("user")
    val user: SimklUser,
    @SerializedName("account")
    val account: SimklAccount
)

data class SimklUser(
    @SerializedName("name")
    val name: String,
    @SerializedName("avatar")
    val avatar: String?
)

data class SimklAccount(
    @SerializedName("id")
    val id: Int,
    @SerializedName("timezone")
    val timezone: String?
)
