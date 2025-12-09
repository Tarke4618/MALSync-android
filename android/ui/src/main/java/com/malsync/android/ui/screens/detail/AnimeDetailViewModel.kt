package com.malsync.android.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malsync.android.domain.model.Anime
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnimeDetailUiState(
    val isLoading: Boolean = false,
    val anime: Anime? = null,
    val error: String? = null
)

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val animeRepository: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: String = checkNotNull(savedStateHandle["animeId"])
    // Assuming we pass provider, or default to MAL for now if not passed
    // Ideally navigation args should include provider
    private val provider: String = savedStateHandle["provider"] ?: SyncProvider.MYANIMELIST.name

    private val _uiState = MutableStateFlow(AnimeDetailUiState())
    val uiState: StateFlow<AnimeDetailUiState> = _uiState.asStateFlow()

    init {
        loadAnimeDetails()
    }

    private fun loadAnimeDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Convert string to enum safe-ish
            val syncProvider = try {
                SyncProvider.valueOf(provider)
            } catch (e: Exception) {
                SyncProvider.MYANIMELIST
            }

            val result = animeRepository.getAnime(syncProvider, animeId)
            
            result.fold(
                onSuccess = { anime ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            anime = anime,
                            error = null
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load details"
                        )
                    }
                }
            )
        }
    }
}
