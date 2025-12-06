package com.malsync.android.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.malsync.android.domain.model.UserAnimeStatus
import com.malsync.android.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isLoading),
            onRefresh = { viewModel.onEvent(ProfileUiEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Provider selector
                item {
                    ProviderSelector(
                        selectedProvider = uiState.selectedProvider,
                        onProviderSelected = { provider ->
                            viewModel.onEvent(ProfileUiEvent.SelectProvider(provider))
                        }
                    )
                }
                
                // Statistics card
                item {
                    StatsCard(stats = uiState.stats)
                }
                
                // Status filter
                item {
                    StatusFilterRow(
                        selectedStatus = uiState.selectedStatus,
                        onStatusSelected = { status ->
                            viewModel.onEvent(ProfileUiEvent.SelectStatus(status))
                        }
                    )
                }
                
                // Error message
                uiState.error?.let { error ->
                    item {
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
                    }
                }
                
                // Anime list
                when {
                    uiState.isLoading && uiState.animeList.isEmpty() -> {
                        item {
                            LoadingState(message = "Loading anime list...")
                        }
                    }
                    
                    uiState.animeList.isEmpty() -> {
                        item {
                            EmptyState(
                                message = "No anime found with status \"${uiState.selectedStatus.name.replace("_", " ").lowercase().capitalize()}\"",
                                icon = "📋"
                            )
                        }
                    }
                    
                    else -> {
                        items(
                            items = uiState.animeList,
                            key = { it.id }
                        ) { anime ->
                            AnimeCard(
                                anime = anime,
                                onClick = {
                                    // TODO: Navigate to anime details
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsCard(
    stats: AnimeStats,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Total",
                    value = stats.totalAnime.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Episodes",
                    value = stats.totalEpisodes.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Watching",
                    value = stats.watching.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Completed",
                    value = stats.completed.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "On Hold",
                    value = stats.onHold.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatusFilterRow(
    selectedStatus: UserAnimeStatus,
    onStatusSelected: (UserAnimeStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Filter by Status",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserAnimeStatus.values().forEach { status ->
                FilterChip(
                    selected = status == selectedStatus,
                    onClick = { onStatusSelected(status) },
                    label = { 
                        Text(
                            text = status.name.replace("_", " ").lowercase()
                                .split(" ")
                                .joinToString(" ") { it.capitalize() }
                        )
                    }
                )
            }
        }
    }
}
