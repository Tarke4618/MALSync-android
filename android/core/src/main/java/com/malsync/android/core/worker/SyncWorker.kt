package com.malsync.android.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.malsync.android.domain.repository.AuthRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val authRepository: AuthRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            authRepository.getAuthenticatedProviders()
                .collect { providers ->
                    providers.forEach { provider ->
                        try {
                             // Attempt to refresh token (placeholder log for now)
                             // authRepository.refreshToken(provider)
                             android.util.Log.i("SyncWorker", "Checking token for $provider")
                        } catch (e: Exception) {
                            android.util.Log.e("SyncWorker", "Failed to refresh $provider", e)
                        }
                    }
                }
            Result.success()
        } catch (e: Exception) {
             Result.retry()
        }
    }
}
