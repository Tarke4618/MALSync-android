package com.malsync.android.data.remote.interceptor

import com.malsync.android.data.local.AuthManager
import com.malsync.android.domain.model.SyncProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Interceptor to add authentication headers to requests
 */
class AuthInterceptor @Inject constructor(private val authManager: AuthManager) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        
        // Determine which provider this request is for
        val provider = when {
            url.host.contains("myanimelist.net") -> SyncProvider.MYANIMELIST
            url.host.contains("anilist.co") -> SyncProvider.ANILIST
            url.host.contains("kitsu.io") -> SyncProvider.KITSU
            url.host.contains("simkl.com") -> SyncProvider.SIMKL
            url.host.contains("shikimori.one") -> SyncProvider.SHIKIMORI
            else -> null
        }
        
        // Get the access token for this provider
        val accessToken = provider?.let { authManager.getToken(it)?.accessToken }
        
        // Add authorization header if token exists
        val newRequest = if (!accessToken.isNullOrBlank()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            request
        }
        
        return chain.proceed(newRequest)
    }
}
