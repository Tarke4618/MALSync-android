package com.malsync.android.data.remote.api

import com.malsync.android.data.remote.dto.kitsu.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Kitsu API Service
 * https://kitsu.docs.apiary.io/
 */
interface KitsuApiService {
    
    @GET("edge/users/{user_id}/library-entries")
    suspend fun getUserAnimeList(
        @Path("user_id") userId: String,
        @Query("filter[kind]") kind: String = "anime",
        @Query("filter[status]") status: String? = null,
        @Query("include") include: String = "anime",
        @Query("page[limit]") limit: Int = 20,
        @Query("page[offset]") offset: Int = 0
    ): Response<KitsuLibraryResponse>
    
    @GET("edge/anime/{anime_id}")
    suspend fun getAnimeDetails(
        @Path("anime_id") animeId: String,
        @Query("include") include: String = "genres,categories"
    ): Response<KitsuAnimeDetailResponse>
    
    @GET("edge/anime")
    suspend fun searchAnime(
        @Query("filter[text]") query: String,
        @Query("page[limit]") limit: Int = 20,
        @Query("page[offset]") offset: Int = 0
    ): Response<KitsuAnimeSearchResponse>
    
    @POST("edge/library-entries")
    @Headers("Content-Type: application/vnd.api+json")
    suspend fun createLibraryEntry(
        @Body body: KitsuLibraryEntryRequest
    ): Response<KitsuLibraryEntryResponse>
    
    @PATCH("edge/library-entries/{entry_id}")
    @Headers("Content-Type: application/vnd.api+json")
    suspend fun updateLibraryEntry(
        @Path("entry_id") entryId: String,
        @Body body: KitsuLibraryEntryRequest
    ): Response<KitsuLibraryEntryResponse>
    
    @DELETE("edge/library-entries/{entry_id}")
    suspend fun deleteLibraryEntry(
        @Path("entry_id") entryId: String
    ): Response<Unit>
    
    @GET("edge/users")
    suspend fun getUserByName(
        @Query("filter[name]") name: String
    ): Response<KitsuUserResponse>
    
    @GET("edge/users/{user_id}")
    suspend fun getUserById(
        @Path("user_id") userId: String
    ): Response<KitsuUserDetailResponse>
    
    @GET("edge/users")
    suspend fun getCurrentUser(
        @Query("filter[self]") self: Boolean = true
    ): Response<KitsuUserResponse>
}
