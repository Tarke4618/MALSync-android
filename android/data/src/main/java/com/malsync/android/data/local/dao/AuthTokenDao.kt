package com.malsync.android.data.local.dao

import androidx.room.*
import com.malsync.android.data.local.entity.AuthTokenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthTokenDao {
    @Query("SELECT * FROM auth_tokens WHERE provider = :provider LIMIT 1")
    suspend fun getAuthToken(provider: String): AuthTokenEntity?
    
    @Query("SELECT * FROM auth_tokens")
    fun getAllAuthTokens(): Flow<List<AuthTokenEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthToken(token: AuthTokenEntity)
    
    @Delete
    suspend fun deleteAuthToken(token: AuthTokenEntity)
    
    @Query("DELETE FROM auth_tokens WHERE provider = :provider")
    suspend fun deleteAuthTokenByProvider(provider: String)
}
