package com.malsync.android.data.remote.api

import com.malsync.android.data.remote.dto.simkl.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Simkl API Service
 * https://simkl.docs.apiary.io/
 */
interface SimklApiService {
    
    @GET("sync/all-items/anime")
    suspend fun getUserAnimeList(
        @Query("extended") extended: String = "full"
    ): Response<SimklAnimeListResponse>
    
    @GET("anime/{anime_id}")
    suspend fun getAnimeDetails(
        @Path("anime_id") animeId: String,
        @Query("extended") extended: String = "full"
    ): Response<SimklAnimeResponse>
    
    @GET("search/anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): Response<List<SimklAnimeSearchResult>>
    
    @POST("sync/history")
    @Headers("Content-Type: application/json")
    suspend fun addToHistory(
        @Body body: SimklHistoryRequest
    ): Response<SimklSyncResponse>
    
    @POST("sync/add-to-list")
    @Headers("Content-Type: application/json")
    suspend fun addToList(
        @Body body: SimklAddToListRequest
    ): Response<SimklSyncResponse>
    
    @POST("sync/remove-from-list")
    @Headers("Content-Type: application/json")
    suspend fun removeFromList(
        @Body body: SimklRemoveFromListRequest
    ): Response<SimklSyncResponse>
    
    @GET("users/settings")
    suspend fun getUserSettings(): Response<SimklUserResponse>
}
