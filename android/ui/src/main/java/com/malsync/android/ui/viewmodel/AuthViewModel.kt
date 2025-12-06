package com.malsync.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun handleOAuthCallback(code: String, state: String?) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // Determine provider from state or URL logic
                // For MVP, we might need to parse this better. 
                // Assuming MAL for now as it's the primary use case requiring this flow.
                // In a real app, 'state' param helps identify the provider.
                
                // Example check (simplified):
                // Default to MyAnimeList if state is unknown for MVP
                val provider = if (state?.lowercase()?.contains("anilist") == true) {
                    SyncProvider.ANILIST
                } else if (state?.lowercase()?.contains("kitsu") == true) {
                    SyncProvider.KITSU
                } else if (state?.lowercase()?.contains("simkl") == true) {
                    SyncProvider.SIMKL
                } else {
                    SyncProvider.MYANIMELIST
                }

                authRepository.exchangeToken(provider, code)
                    .onSuccess {
                        _authState.value = AuthState.Success
                    }
                    .onFailure { error ->
                        _authState.value = AuthState.Error(error.message ?: "Token exchange failed")
                    }
                
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
