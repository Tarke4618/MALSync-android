package com.malsync.android.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.malsync.android.domain.model.AuthToken
import com.malsync.android.domain.model.SyncProvider
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _authStates = MutableStateFlow<Map<SyncProvider, AuthToken?>>(emptyMap())
    val authStates: StateFlow<Map<SyncProvider, AuthToken?>> = _authStates.asStateFlow()

    init {
        loadTokens()
    }

    private fun loadTokens() {
        val newMap = mutableMapOf<SyncProvider, AuthToken?>()
        SyncProvider.values().forEach { provider ->
            val json = sharedPreferences.getString(provider.name, null)
            if (json != null) {
                try {
                    val token = gson.fromJson(json, AuthToken::class.java)
                    if (token != null && !token.isExpired()) {
                        newMap[provider] = token
                    } else {
                        // Clear expired token? Or keep it for refresh?
                        // For now, keep it if refresh token exists, else clear
                        if (token?.refreshToken != null) {
                             newMap[provider] = token
                        } else {
                             removeToken(provider)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        _authStates.value = newMap
    }

    fun saveToken(token: AuthToken) {
        val json = gson.toJson(token)
        sharedPreferences.edit().putString(token.provider.name, json).apply()
        loadTokens()
    }

    fun removeToken(provider: SyncProvider) {
        sharedPreferences.edit().remove(provider.name).apply()
        loadTokens()
    }

    fun getToken(provider: SyncProvider): AuthToken? {
        return _authStates.value[provider]
    }
    
    fun isAuthenticated(provider: SyncProvider): Boolean {
        return getToken(provider) != null
    }
}
