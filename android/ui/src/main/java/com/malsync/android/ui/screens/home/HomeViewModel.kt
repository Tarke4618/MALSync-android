package com.malsync.android.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malsync.android.domain.model.Anime
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.model.UserAnimeStatus
import com.malsync.android.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val watchingAnime: List<Anime> = emptyList(),
    val selectedProvider: SyncProvider = SyncProvider.MYANIMELIST,
    val error: String? = null,
    val isSyncing: Boolean = false
)

sealed class HomeUiEvent {
    data class SelectProvider(val provider: SyncProvider) : HomeUiEvent()
    object Refresh : HomeUiEvent()
    object Sync : HomeUiEvent()
    data class UpdateEpisode(val animeId: String, val episode: Int) : HomeUiEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadWatchingAnime()
    }
    
    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.SelectProvider -> {
                _uiState.update { it.copy(selectedProvider = event.provider) }
                loadWatchingAnime()
            }
            is HomeUiEvent.Refresh -> {
                loadWatchingAnime()
            }
            is HomeUiEvent.Sync -> {
                syncAnimeList()
            }
            is HomeUiEvent.UpdateEpisode -> {
                updateAnimeProgress(event.animeId, event.episode)
            }
        }
    }
    
    private fun loadWatchingAnime() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            animeRepository.getAnimeList(
                provider = _uiState.value.selectedProvider,
                status = UserAnimeStatus.WATCHING
            ).catch { e ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }.collect { animeList ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        watchingAnime = animeList.sortedByDescending { anime -> anime.lastUpdated },
                        error = null
                    )
                }
            }
        }
    }
    
    private fun syncAnimeList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            
            val result = animeRepository.syncAnimeList(_uiState.value.selectedProvider)
            
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isSyncing = false) }
                    loadWatchingAnime()
                },
                onFailure = { e ->
                    _uiState.update { 
                        it.copy(
                            isSyncing = false,
                            error = e.message ?: "Sync failed"
                        )
                    }
                }
            )
        }
    }
    
    private fun updateAnimeProgress(animeId: String, episode: Int) {
        viewModelScope.launch {
            val result = animeRepository.updateAnimeProgress(
                provider = _uiState.value.selectedProvider,
                animeId = animeId,
                episode = episode,
                status = UserAnimeStatus.WATCHING
            )
            
            result.fold(
                onSuccess = { /* Auto-updates via Flow */ },
                onFailure = { e ->
                    _uiState.update { 
                        it.copy(error = e.message ?: "Update failed")
                    }
                }
            )
        }
    }
}
