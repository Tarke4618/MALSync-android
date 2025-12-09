package com.malsync.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.ui.theme.*

@Composable
fun ProviderSelector(
    selectedProvider: SyncProvider,
    onProviderSelected: (SyncProvider) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Sync Provider",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SyncProvider.values().forEach { provider ->
                    ProviderChip(
                        provider = provider,
                        isSelected = provider == selectedProvider,
                        onClick = { onProviderSelected(provider) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProviderChip(
    provider: SyncProvider,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        getProviderColor(provider)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    val contentColor = if (isSelected) {
        Color.White
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Provider Icon (using first letter as placeholder)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = provider.displayName.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = when (provider) {
                    SyncProvider.MYANIMELIST -> "MAL"
                    SyncProvider.ANILIST -> "AniList"
                    SyncProvider.KITSU -> "Kitsu"
                    SyncProvider.SIMKL -> "Simkl"
                    SyncProvider.SHIKIMORI -> "Shikimori"
                },
                style = MaterialTheme.typography.labelSmall,
                color = contentColor
            )
        }
    }
}

@Composable
private fun getProviderColor(provider: SyncProvider): Color {
    return when (provider) {
        SyncProvider.MYANIMELIST -> ColorMAL
        SyncProvider.ANILIST -> ColorAniList
        SyncProvider.KITSU -> ColorKitsu
        SyncProvider.SIMKL -> ColorSimkl
        SyncProvider.SHIKIMORI -> ColorShikimori
    }
}
