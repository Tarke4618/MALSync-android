package com.malsync.android.data.remote.api

import com.malsync.android.data.remote.dto.mal.*
import retrofit2.Response
import retrofit2.http.*

/**
 * MyAnimeList API v2 Service
 * https://myanimelist.net/apiconfig/references/api/v2
 */
interface MalApiService {
    
    @GET("users/@me/animelist")
    suspend fun getUserAnimeList(
        @Query("status") status: String? = null,
        @Query("sort") sort: String = "list_updated_at",
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("fields") fields: String = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,related_manga,recommendations,studios,statistics"
    ): Response<MalAnimeListResponse>
    
    @GET("anime/{anime_id}")
    suspend fun getAnimeDetails(
        @Path("anime_id") animeId: String,
        @Query("fields") fields: String = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,related_manga,recommendations,studios,statistics"
    ): Response<MalAnimeResponse>
    
    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("fields") fields: String = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,media_type,status,genres,num_episodes,start_season"
    ): Response<MalAnimeSearchResponse>
    
    @PATCH("anime/{anime_id}/my_list_status")
    @FormUrlEncoded
    suspend fun updateAnimeListStatus(
        @Path("anime_id") animeId: String,
        @Field("status") status: String? = null,
        @Field("is_rewatching") isRewatching: Boolean? = null,
        @Field("score") score: Int? = null,
        @Field("num_watched_episodes") numWatchedEpisodes: Int? = null,
        @Field("priority") priority: Int? = null,
        @Field("num_times_rewatched") numTimesRewatched: Int? = null,
        @Field("rewatch_value") rewatchValue: Int? = null,
        @Field("tags") tags: String? = null,
        @Field("comments") comments: String? = null
    ): Response<MalListStatusResponse>
    
    @DELETE("anime/{anime_id}/my_list_status")
    suspend fun deleteAnimeFromList(
        @Path("anime_id") animeId: String
    ): Response<Unit>
    
    @GET("users/@me/mangalist")
    suspend fun getUserMangaList(
        @Query("status") status: String? = null,
        @Query("sort") sort: String = "list_updated_at",
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("fields") fields: String = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_volumes,num_chapters,authors{first_name,last_name},pictures,background,related_anime,related_manga,recommendations,serialization{name}"
    ): Response<MalMangaListResponse>
    
    @GET("manga/{manga_id}")
    suspend fun getMangaDetails(
        @Path("manga_id") mangaId: String,
        @Query("fields") fields: String = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_volumes,num_chapters,authors{first_name,last_name},pictures,background,related_anime,related_manga,recommendations,serialization{name}"
    ): Response<MalMangaResponse>
    
    @PATCH("manga/{manga_id}/my_list_status")
    @FormUrlEncoded
    suspend fun updateMangaListStatus(
        @Path("manga_id") mangaId: String,
        @Field("status") status: String? = null,
        @Field("is_rereading") isRereading: Boolean? = null,
        @Field("score") score: Int? = null,
        @Field("num_volumes_read") numVolumesRead: Int? = null,
        @Field("num_chapters_read") numChaptersRead: Int? = null,
        @Field("priority") priority: Int? = null,
        @Field("num_times_reread") numTimesReread: Int? = null,
        @Field("reread_value") rereadValue: Int? = null,
        @Field("tags") tags: String? = null,
        @Field("comments") comments: String? = null
    ): Response<MalListStatusResponse>
    
    @GET("users/@me")
    suspend fun getUserProfile(
        @Query("fields") fields: String = "id,name,picture,gender,birthday,location,joined_at,anime_statistics,time_zone,is_supporter"
    ): Response<MalUserProfile>
}
