package com.malsync.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.malsync.android.domain.model.Anime

@Composable
fun AnimeCard(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onIncrementEpisode: (() -> Unit)? = null,
    showProgress: Boolean = true
) {
    Card(
        modifier = modifier
            .width(160.dp) // Standard poster width
            .height(260.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat for modern look, use border/shadow only if needed
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Image Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Take up available space
            ) {
                AsyncImage(
                    model = anime.coverImage,
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Status Badge
                anime.userStatus?.let { status ->
                    Surface(
                        color = getStatusColor(status.name).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = status.name.replace("_", " "),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                
                // Episode Count Badge (Glassmorphismish)
                if (anime.episodes != null || anime.currentEpisode > 0) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "${anime.currentEpisode}" + (anime.episodes?.let { "/$it" } ?: "+"),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                
                // Progress Bar Overlay
                if (showProgress) {
                   val progress = if (anime.episodes != null && anime.episodes > 0) {
                        anime.currentEpisode.toFloat() / anime.episodes.toFloat()
                    } else {
                        0f
                    }
                    if (progress > 0) {
                         LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .align(Alignment.BottomCenter),
                            trackColor = Color.Transparent,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Text Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2 // Fix height for grid alignment
                )
                
                if (onIncrementEpisode != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = onIncrementEpisode,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                         Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ep",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("1 Ep", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(
    text: String,
    containerColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = containerColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

@Composable
private fun getStatusColor(status: String): androidx.compose.ui.graphics.Color {
    return when (status) {
        "WATCHING" -> MaterialTheme.colorScheme.primaryContainer
        "COMPLETED" -> MaterialTheme.colorScheme.tertiaryContainer
        "ON_HOLD" -> MaterialTheme.colorScheme.secondaryContainer
        "DROPPED" -> MaterialTheme.colorScheme.errorContainer
        "PLAN_TO_WATCH" -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}
