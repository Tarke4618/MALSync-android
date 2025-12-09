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
    val isLoading: Boolean = false,
    val showUpdateProposal: Boolean = false,
    val injectionScript: String? = null
)

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val streamingSiteRepository: StreamingSiteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowserUiState())
    val uiState: StateFlow<BrowserUiState> = _uiState.asStateFlow()

    fun onUrlChanged(url: String) {
        // Reset proposal when URL changes
        _uiState.update { it.copy(currentUrl = url, showUpdateProposal = false, injectionScript = null) }
        checkUrl(url)
    }

    fun onPageFinished(url: String) {
        viewModelScope.launch {
            val script = streamingSiteRepository.getInjectionScript(url)
            if (script != null) {
                _uiState.update { it.copy(injectionScript = script) }
            }
        }
    }
    
    fun onVideoFound(duration: Float) {
        // Optional: Perform actions when video is first found
    }

    fun onVideoProgress(current: Float, total: Float) {
        if (total > 0) {
            val progress = current / total
            // Trigger at 80% completion
            if (progress >= 0.8f && !_uiState.value.showUpdateProposal) {
                _uiState.update { it.copy(showUpdateProposal = true) }
            }
        }
    }
    
    fun onScriptInjected() {
        _uiState.update { it.copy(injectionScript = null) }
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
