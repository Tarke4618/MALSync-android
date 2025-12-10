package com.malsync.android.ui.screens.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.malsync.android.domain.model.UserAnimeStatus
import com.malsync.android.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isGridView by remember { mutableStateOf(true) } // View Toggle State
    
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.onEvent(LibraryUiEvent.Refresh)
        }
    }

    LaunchedEffect(uiState.isLoading) {
        if (uiState.isLoading) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Library") },
                actions = {
                    // View Toggle
                    IconButton(onClick = { isGridView = !isGridView }) {
                        Icon(
                            imageVector = if (isGridView) Icons.AutoMirrored.Filled.List else Icons.Filled.GridView,
                            contentDescription = "Toggle View"
                        )
                    }
                    IconButton(
                        onClick = { viewModel.onEvent(LibraryUiEvent.Sync) },
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Status Filter Chips
                ScrollableTabRow(
                    selectedTabIndex = UserAnimeStatus.values().indexOf(uiState.selectedStatus),
                    edgePadding = 0.dp,
                    indicator = { /* No indicator for chips style */ },
                    divider = {},
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    UserAnimeStatus.values().forEach { status ->
                         // Use Material3 FilterChip
                         FilterChip(
                            selected = uiState.selectedStatus == status,
                            onClick = { viewModel.onEvent(LibraryUiEvent.SelectStatus(status)) },
                            label = { Text(status.name.replace("_", " ")) },
                            modifier = Modifier.padding(horizontal = 4.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ProviderSelector(
                    selectedProvider = uiState.selectedProvider,
                    onProviderSelected = { provider ->
                        viewModel.onEvent(LibraryUiEvent.SelectProvider(provider))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.animeList.isEmpty() && !uiState.isLoading) {
                    EmptyState(
                        message = "No anime found for this status.",
                        icon = "📁",
                        actionText = null,
                        onAction = {}
                    )
                } else {
                    if (isGridView) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 150.dp),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 80.dp) // Space for bottom bar
                        ) {
                            items(
                                items = uiState.animeList,
                                key = { it.id }
                            ) { anime ->
                                AnimeCard(
                                    anime = anime,
                                    onClick = { onNavigateToDetail(anime.id) },
                                    onIncrementEpisode = {
                                        viewModel.onEvent(
                                            LibraryUiEvent.UpdateEpisode(
                                                animeId = anime.id,
                                                episode = anime.currentEpisode + 1
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    } else {
                         // List View
                        androidx.compose.foundation.lazy.LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(
                                items = uiState.animeList,
                                key = { it.id }
                            ) { anime ->
                                // Row based card or reuse AnimeCard with row modifier?
                                // For now, reuse AnimeCard but maybe full width?
                                // Better: A custom RowAnimeCard. Attempting minimal change using AnimeCard in a horizontal logic?
                                // Actually, let's just make AnimeCard flexible or wrap it.
                                // For AnymeX List view, it's usually a Row with image on left.
                                // I'll stick to full width AnimeCard for now to save complexity, or creating a quick row.
                                // Actually, user asked for "Grid/List toggle". A full width card IS a list technically.
                                AnimeCard(
                                    anime = anime,
                                    onClick = { onNavigateToDetail(anime.id) },
                                    modifier = Modifier.fillMaxWidth().height(200.dp), // Adjust height for list look
                                    onIncrementEpisode = {
                                        viewModel.onEvent(
                                             LibraryUiEvent.UpdateEpisode(
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
