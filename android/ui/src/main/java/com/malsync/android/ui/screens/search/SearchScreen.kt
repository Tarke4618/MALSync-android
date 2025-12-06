package com.malsync.android.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.malsync.android.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Anime") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search bar
            OutlinedTextField(
                value = uiState.query,
                onValueChange = { viewModel.onEvent(SearchUiEvent.QueryChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search for anime...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (uiState.query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onEvent(SearchUiEvent.ClearSearch) }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.onEvent(SearchUiEvent.Search)
                        keyboardController?.hide()
                    }
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Provider filter chips
            ProviderFilterChips(
                selectedProviders = uiState.selectedProviders,
                onProviderToggled = { provider ->
                    viewModel.onEvent(SearchUiEvent.ProviderToggled(provider))
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search button
            Button(
                onClick = {
                    viewModel.onEvent(SearchUiEvent.Search)
                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.query.isNotEmpty() && uiState.selectedProviders.isNotEmpty() && !uiState.isSearching
            ) {
                if (uiState.isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Searching...")
                } else {
                    Text("Search")
                }
            }
            
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
            
            // Results
            when {
                uiState.isSearching -> {
                    LoadingState(message = "Searching anime...")
                }
                
                !uiState.hasSearched -> {
                    EmptyState(
                        message = "Search for your favorite anime!",
                        icon = "🔍"
                    )
                }
                
                uiState.searchResults.isEmpty() -> {
                    EmptyState(
                        message = "No results found for \"${uiState.query}\"",
                        icon = "😕"
                    )
                }
                
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = uiState.searchResults,
                            key = { "${it.provider.name}_${it.id}" }
                        ) { anime ->
                            AnimeCard(
                                anime = anime,
                                onClick = {
                                    // TODO: Navigate to anime details or add to list
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProviderFilterChips(
    selectedProviders: Set<com.malsync.android.domain.model.SyncProvider>,
    onProviderToggled: (com.malsync.android.domain.model.SyncProvider) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        com.malsync.android.domain.model.SyncProvider.values().forEach { provider ->
            FilterChip(
                selected = selectedProviders.contains(provider),
                onClick = { onProviderToggled(provider) },
                label = { Text(provider.displayName) }
            )
        }
    }
}
