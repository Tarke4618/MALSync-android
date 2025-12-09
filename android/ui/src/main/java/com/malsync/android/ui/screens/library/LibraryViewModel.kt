package com.malsync.android.ui.screens.library

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

data class LibraryUiState(
    val isLoading: Boolean = false,
    val animeList: List<Anime> = emptyList(),
    val selectedProvider: SyncProvider = SyncProvider.MYANIMELIST,
    val selectedStatus: UserAnimeStatus = UserAnimeStatus.WATCHING,
    val error: String? = null,
    val isSyncing: Boolean = false
)

sealed class LibraryUiEvent {
    data class SelectProvider(val provider: SyncProvider) : LibraryUiEvent()
    data class SelectStatus(val status: UserAnimeStatus) : LibraryUiEvent()
    object Refresh : LibraryUiEvent()
    object Sync : LibraryUiEvent()
    data class UpdateEpisode(val animeId: String, val episode: Int) : LibraryUiEvent()
}

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()
    
    init {
        loadAnimeList()
    }
    
    fun onEvent(event: LibraryUiEvent) {
        when (event) {
            is LibraryUiEvent.SelectProvider -> {
                _uiState.update { it.copy(selectedProvider = event.provider) }
                loadAnimeList()
            }
            is LibraryUiEvent.SelectStatus -> {
                _uiState.update { it.copy(selectedStatus = event.status) }
                loadAnimeList()
            }
            is LibraryUiEvent.Refresh -> {
                loadAnimeList()
            }
            is LibraryUiEvent.Sync -> {
                syncAnimeList()
            }
            is LibraryUiEvent.UpdateEpisode -> {
                updateAnimeProgress(event.animeId, event.episode)
            }
        }
    }
    
    private fun loadAnimeList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            animeRepository.getAnimeList(
                provider = _uiState.value.selectedProvider,
                status = _uiState.value.selectedStatus
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
                        animeList = animeList.sortedByDescending { anime -> anime.lastUpdated },
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
                    loadAnimeList()
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
                status = _uiState.value.selectedStatus
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
