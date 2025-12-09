package com.malsync.android.ui.screens.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malsync.android.domain.model.SyncProvider
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: com.malsync.android.domain.repository.AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val providerStates: StateFlow<Map<SyncProvider, Boolean>> = authRepository.getAuthenticatedProviders()
        .map { authenticatedProviders ->
            SyncProvider.values().associateWith { provider ->
                authenticatedProviders.contains(provider)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    fun onConnectClick(provider: SyncProvider) {
        val url = getDoAuthUrl(provider)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun getDoAuthUrl(provider: SyncProvider): String {
        // NOTE: These Client IDs should ideally come from BuildConfig / gradle.properties
        // User must replace these placeholders!
        val callbackUrl = "malsync://oauth"
        
        return when (provider) {
            SyncProvider.MYANIMELIST -> {
                "${provider.oauthUrl}?response_type=code&client_id=MY_MAL_CLIENT_ID&state=myanimelist&code_challenge=CODE_CHALLENGE" 
                // MAL requires PKCE, simplified here. 
            }
            SyncProvider.ANILIST -> {
                "${provider.oauthUrl}?client_id=MY_ANILIST_ID&response_type=code&redirect_uri=$callbackUrl&state=anilist"
            }
            SyncProvider.KITSU -> {
                 "${provider.oauthUrl}?client_id=MY_KITSU_ID&response_type=code&redirect_uri=$callbackUrl&state=kitsu"
            }
            SyncProvider.SIMKL -> {
                 "${provider.oauthUrl}?response_type=code&client_id=MY_SIMKL_ID&redirect_uri=$callbackUrl&state=simkl"
            }
            SyncProvider.SHIKIMORI -> {
                 "${provider.oauthUrl}?client_id=MY_SHIKIMORI_ID&redirect_uri=$callbackUrl&response_type=code&scope=user_rates&state=shikimori"
            }
        }
    }
    
    fun onDisconnectClick(provider: SyncProvider) {
        viewModelScope.launch {
            authRepository.deleteAuthToken(provider)
        }
    }
}
