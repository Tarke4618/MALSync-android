package com.malsync.android.domain.usecase

import com.malsync.android.domain.model.Anime
import com.malsync.android.domain.repository.AnimeRepository
import com.malsync.android.domain.repository.StreamingSiteRepository
import javax.inject.Inject

/**
 * Use case for detecting anime from streaming site URL
 * Ported from extension's content detection logic
 */
class DetectAnimeFromUrlUseCase @Inject constructor(
    private val streamingSiteRepository: StreamingSiteRepository,
    private val animeRepository: AnimeRepository
) {
    
    suspend operator fun invoke(url: String): Result<Anime?> {
        return try {
            // Check if site is supported
            if (!streamingSiteRepository.isSiteSupported(url)) {
                return Result.success(null)
            }
            
            // Detect content from URL
            // Detect content from URL
            if (streamingSiteRepository.detectContent(url) == null) {
                return Result.success(null)
            }
            
            // Try to find matching anime in user's list or database
            val anime = animeRepository.getAnimeByUrl(url).getOrNull()
            
            Result.success(anime)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
