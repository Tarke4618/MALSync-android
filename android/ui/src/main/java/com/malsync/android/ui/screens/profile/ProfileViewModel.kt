package com.malsync.android.ui.screens.profile

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

data class ProfileUiState(
    val selectedProvider: SyncProvider = SyncProvider.MYANIMELIST,
    val selectedStatus: UserAnimeStatus = UserAnimeStatus.WATCHING,
    val animeList: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val stats: AnimeStats = AnimeStats()
)

data class AnimeStats(
    val totalAnime: Int = 0,
    val watching: Int = 0,
    val completed: Int = 0,
    val onHold: Int = 0,
    val dropped: Int = 0,
    val planToWatch: Int = 0,
    val totalEpisodes: Int = 0
)

sealed class ProfileUiEvent {
    data class SelectProvider(val provider: SyncProvider) : ProfileUiEvent()
    data class SelectStatus(val status: UserAnimeStatus) : ProfileUiEvent()
    object Refresh : ProfileUiEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadAnimeList()
        loadStats()
    }
    
    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.SelectProvider -> {
                _uiState.update { it.copy(selectedProvider = event.provider) }
                loadAnimeList()
                loadStats()
            }
            
            is ProfileUiEvent.SelectStatus -> {
                _uiState.update { it.copy(selectedStatus = event.status) }
                loadAnimeList()
            }
            
            is ProfileUiEvent.Refresh -> {
                loadAnimeList()
                loadStats()
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
                        error = e.message ?: "Failed to load anime list"
                    )
                }
            }.collect { animeList ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        animeList = animeList,
                        error = null
                    )
                }
            }
        }
    }
    
    private fun loadStats() {
        viewModelScope.launch {
            animeRepository.getAnimeList(
                provider = _uiState.value.selectedProvider,
                status = null
            ).catch { }
            .collect { allAnime ->
                val stats = AnimeStats(
                    totalAnime = allAnime.size,
                    watching = allAnime.count { it.userStatus == UserAnimeStatus.WATCHING },
                    completed = allAnime.count { it.userStatus == UserAnimeStatus.COMPLETED },
                    onHold = allAnime.count { it.userStatus == UserAnimeStatus.ON_HOLD },
                    dropped = allAnime.count { it.userStatus == UserAnimeStatus.DROPPED },
                    planToWatch = allAnime.count { it.userStatus == UserAnimeStatus.PLAN_TO_WATCH },
                    totalEpisodes = allAnime.sumOf { it.currentEpisode }
                )
                _uiState.update { it.copy(stats = stats) }
            }
        }
    }
}
