package com.malsync.android.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.malsync.android.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToBrowser: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Currently Watching") },
                actions = {
                    // Sync button
                    IconButton(
                        onClick = { viewModel.onEvent(HomeUiEvent.Sync) },
                        enabled = !uiState.isSyncing
                    ) {
                        if (uiState.isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Filled.Sync, contentDescription = "Sync")
                        }
                    }
                    
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isLoading),
            onRefresh = { viewModel.onEvent(HomeUiEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Provider Selector
                Spacer(modifier = Modifier.height(16.dp))
                
                ProviderSelector(
                    selectedProvider = uiState.selectedProvider,
                    onProviderSelected = { provider ->
                        viewModel.onEvent(HomeUiEvent.SelectProvider(provider))
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Error message
                uiState.error?.let { error ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Content
                when {
                    uiState.isLoading && uiState.watchingAnime.isEmpty() -> {
                        LoadingState(message = "Loading your anime list...")
                    }
                    
                    uiState.watchingAnime.isEmpty() -> {
                        EmptyState(
                            message = "No anime in your watching list.\nStart tracking anime to see them here!",
                            icon = "📺",
                            actionText = "Browse Anime",
                            onAction = { /* TODO: Navigate to search */ }
                        )
                    }
                    
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(
                                items = uiState.watchingAnime,
                                key = { it.id }
                            ) { anime ->
                                AnimeCard(
                                    anime = anime,
                                    onClick = {
                                        // TODO: Navigate to anime details
                                    },
                                    onIncrementEpisode = {
                                        viewModel.onEvent(
                                            HomeUiEvent.UpdateEpisode(
                                                animeId = anime.id,
                                                episode = anime.currentEpisode + 1
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
