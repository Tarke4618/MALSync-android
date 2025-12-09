package com.malsync.android.data.remote.api

import com.malsync.android.data.remote.dto.shikimori.ShikimoriAnime
import com.malsync.android.data.remote.dto.shikimori.ShikimoriUserProfile
import com.malsync.android.data.remote.dto.shikimori.ShikimoriUserRate
import retrofit2.Response
import retrofit2.http.*

interface ShikimoriApiService {

    @GET("users/whoami")
    suspend fun getWhoAmI(): Response<ShikimoriUserProfile>

    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") id: Int): Response<ShikimoriUserProfile>

    @GET("users/{user_id}/anime_rates")
    suspend fun getUserAnimeRates(
        @Path("user_id") userId: Int,
        @Query("limit") limit: Int = 100,
        @Query("status") status: String? = null,
        @Query("page") page: Int = 1
    ): Response<List<ShikimoriUserRate>>

    @GET("animes")
    suspend fun searchAnime(
        @Query("search") search: String,
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1
    ): Response<List<ShikimoriAnime>>

    @GET("animes/{id}")
    suspend fun getAnimeDetails(@Path("id") id: Int): Response<ShikimoriAnime>
    
    // Create/Update rate
    @POST("v2/user_rates")
    suspend fun createUserRate(@Body rate: ShikimoriUserRate): Response<ShikimoriUserRate>

    @PATCH("v2/user_rates/{id}")
    suspend fun updateUserRate(
        @Path("id") id: Int,
        @Body rate: ShikimoriUserRate
    ): Response<ShikimoriUserRate>
}
