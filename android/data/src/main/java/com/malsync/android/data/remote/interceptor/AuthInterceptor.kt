package com.malsync.android.data.remote.interceptor

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.malsync.android.domain.model.SyncProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

private val Context.dataStore by preferencesDataStore(name = "auth_tokens")

/**
 * Interceptor to add authentication headers to requests
 */
class AuthInterceptor(private val context: Context) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        
        // Determine which provider this request is for
        val provider = when {
            url.host.contains("myanimelist.net") -> SyncProvider.MYANIMELIST
            url.host.contains("anilist.co") -> SyncProvider.ANILIST
            url.host.contains("kitsu.io") -> SyncProvider.KITSU
            url.host.contains("simkl.com") -> SyncProvider.SIMKL
            else -> null
        }
        
        // Get the access token for this provider
        val accessToken = provider?.let { getAccessToken(it) }
        
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
    
    private fun getAccessToken(provider: SyncProvider): String? = runBlocking {
        val key = stringPreferencesKey("${provider.name}_access_token")
        context.dataStore.data.map { preferences ->
            preferences[key]
        }.first()
    }
}
