package com.malsync.android.domain.usecase

import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.model.UserAnimeStatus
import com.malsync.android.domain.repository.AnimeRepository
import javax.inject.Inject

/**
 * Use case for syncing episode progress
 * Core feature ported from extension's auto-tracking
 */
class SyncEpisodeUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    
    /**
     * Update episode progress across all authenticated providers
     *
     * @param animeId Anime ID
     * @param episode New episode number
     * @param provider Primary provider
     * @param autoUpdateStatus If true, mark as completed when all episodes watched
     */
    suspend operator fun invoke(
        animeId: String,
        episode: Int,
        provider: SyncProvider,
        autoUpdateStatus: Boolean = true
    ): Result<Unit> {
        return try {
            // Get anime details
            val anime = animeRepository.getAnime(provider, animeId).getOrThrow()
            
            // Determine if should mark as completed
            val newStatus = if (autoUpdateStatus && anime.episodes != null && episode >= anime.episodes) {
                UserAnimeStatus.COMPLETED
            } else {
                anime.userStatus
            }
            
            // Update progress
            animeRepository.updateAnimeProgress(
                provider = provider,
                animeId = animeId,
                episode = episode,
                status = newStatus
            )
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
