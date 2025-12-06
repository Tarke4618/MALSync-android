package com.malsync.android.ui.screens.browser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malsync.android.domain.model.DetectedContent
import com.malsync.android.domain.repository.StreamingSiteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BrowserUiState(
    val currentUrl: String = "",
    val detectedContent: DetectedContent? = null,
    val isSiteSupported: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val streamingSiteRepository: StreamingSiteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowserUiState())
    val uiState: StateFlow<BrowserUiState> = _uiState.asStateFlow()

    fun onUrlChanged(url: String) {
        _uiState.update { it.copy(currentUrl = url) }
        checkUrl(url)
    }

    private fun checkUrl(url: String) {
        viewModelScope.launch {
            val isSupported = streamingSiteRepository.isSiteSupported(url)
            val detected = if (isSupported) {
                streamingSiteRepository.detectContent(url)
            } else {
                null
            }

            _uiState.update { 
                it.copy(
                    isSiteSupported = isSupported,
                    detectedContent = detected
                )
            }
        }
    }
}
