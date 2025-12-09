package com.malsync.android.ui.screens.discover

import androidx.lifecycle.ViewModel
import com.malsync.android.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val trendingAnime: List<Anime> = emptyList(),
    val seasonalAnime: List<Anime> = emptyList(),
    val topRatedAnime: List<Anime> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class DiscoverViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()
    
    init {
        // TODO: Implement actual data fetching when Repository supports it
        // For now we leave lists empty or mock if needed for visual verification
    }
}
