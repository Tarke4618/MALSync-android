package com.malsync.android.ui.screens.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.malsync.android.domain.model.Anime
import com.malsync.android.ui.components.AnimeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discover") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            
            DiscoverSection(
                title = "Trending Now",
                animeList = uiState.trendingAnime,
                onAnimeClick = onNavigateToDetail
            )

            DiscoverSection(
                title = "Seasonal Anime",
                animeList = uiState.seasonalAnime,
                onAnimeClick = onNavigateToDetail
            )

            DiscoverSection(
                title = "Top Rated",
                animeList = uiState.topRatedAnime,
                onAnimeClick = onNavigateToDetail
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun DiscoverSection(
    title: String,
    animeList: List<Anime>,
    onAnimeClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        if (animeList.isEmpty()) {
            // Placeholder empty state for "Skeleton" loading look or just message
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "Coming Soon",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(animeList) { anime ->
                    Box(modifier = Modifier.width(140.dp)) {
                        AnimeCard(
                            anime = anime,
                            onClick = { onAnimeClick(anime.id) },
                            onIncrementEpisode = { /* Disable quick increment in discover */ }
                        )
                    }
                }
            }
        }
    }
}
