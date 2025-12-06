package com.malsync.android.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malsync.android.domain.model.Anime
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val searchResults: List<Anime> = emptyList(),
    val isSearching: Boolean = false,
    val selectedProviders: Set<SyncProvider> = setOf(SyncProvider.MYANIMELIST),
    val error: String? = null,
    val hasSearched: Boolean = false
)

sealed class SearchUiEvent {
    data class QueryChanged(val query: String) : SearchUiEvent()
    data class ProviderToggled(val provider: SyncProvider) : SearchUiEvent()
    object Search : SearchUiEvent()
    object ClearSearch : SearchUiEvent()
}

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.QueryChanged -> {
                _uiState.update { it.copy(query = event.query) }
            }
            
            is SearchUiEvent.ProviderToggled -> {
                val currentProviders = _uiState.value.selectedProviders.toMutableSet()
                if (currentProviders.contains(event.provider)) {
                    currentProviders.remove(event.provider)
                } else {
                    currentProviders.add(event.provider)
                }
                _uiState.update { it.copy(selectedProviders = currentProviders) }
            }
            
            is SearchUiEvent.Search -> {
                searchAnime()
            }
            
            is SearchUiEvent.ClearSearch -> {
                _uiState.update { 
                    SearchUiState(
                        selectedProviders = it.selectedProviders
                    )
                }
            }
        }
    }
    
    private fun searchAnime() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, error = null) }
            
            val result = animeRepository.searchAnime(
                query = query,
                providers = _uiState.value.selectedProviders.toList()
            )
            
            result.fold(
                onSuccess = { results ->
                    _uiState.update { 
                        it.copy(
                            isSearching = false,
                            searchResults = results,
                            hasSearched = true,
                            error = null
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { 
                        it.copy(
                            isSearching = false,
                            error = e.message ?: "Search failed",
                            hasSearched = true
                        )
                    }
                }
            )
        }
    }
}
