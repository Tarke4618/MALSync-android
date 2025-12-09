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
@Composable
fun AnimeCard(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onIncrementEpisode: (() -> Unit)? = null,
    showProgress: Boolean = true
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f, 
        label = "cardScale"
    )

    Card(
        modifier = modifier
            .width(160.dp)
            .height(260.dp)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = anime.coverImage,
                contentDescription = anime.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            // Status Badge (Glass)
            anime.userStatus?.let { status ->
                 Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .com.malsync.android.ui.theme.glassBackground(
                            shape = RoundedCornerShape(8.dp),
                            color = getStatusColor(status.name).copy(alpha = 0.7f),
                            borderColor = Color.White.copy(alpha = 0.2f)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = status.name.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }

            // Episode Badge (Glass)
            if (anime.episodes != null || anime.currentEpisode > 0) {
                 Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .com.malsync.android.ui.theme.glassBackground(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${anime.currentEpisode}" + (anime.episodes?.let { "/$it" } ?: "+"),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }

            // Bottom Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (showProgress) {
                    Spacer(modifier = Modifier.height(8.dp))
                    val episodes = anime.episodes
                    val progress = if (episodes != null && episodes > 0) {
                        anime.currentEpisode.toFloat() / episodes.toFloat()
                    } else {
                        0f
                    }
                    if (progress > 0) {
                         LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            trackColor = Color.White.copy(alpha = 0.3f),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                 if (onIncrementEpisode != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Glass Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .clickable { onIncrementEpisode() }
                            .com.malsync.android.ui.theme.glassBackground(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                         Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Ep",
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("1 Ep", style = MaterialTheme.typography.labelMedium, color = Color.White)
                        }
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
