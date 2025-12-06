package com.malsync.android.domain.repository

import com.malsync.android.domain.model.AuthToken
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository for authentication operations
 */
interface AuthRepository {
    
    /**
     * Check if user is authenticated with a provider
     */
    fun isAuthenticated(provider: SyncProvider): Flow<Boolean>
    
    /**
     * Get current auth token for a provider
     */
    suspend fun getAuthToken(provider: SyncProvider): AuthToken?
    
    /**
     * Save auth token
     */
    suspend fun saveAuthToken(token: AuthToken)
    
    /**
     * Delete auth token (logout)
     */
    suspend fun deleteAuthToken(provider: SyncProvider)
    
    /**
     * Get user profile
     */
    suspend fun getUserProfile(provider: SyncProvider): Result<UserProfile>
    
    /**
     * Refresh expired token
     */
    suspend fun refreshToken(provider: SyncProvider): Result<AuthToken>
    
    /**
     * Get all authenticated providers
     */
    fun getAuthenticatedProviders(): Flow<List<SyncProvider>>
}
