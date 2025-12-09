package com.malsync.android.data.local.dao

import androidx.room.*
import com.malsync.android.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE provider = :provider LIMIT 1")
    suspend fun getUserProfile(provider: String): UserProfileEntity?
    
    @Query("SELECT * FROM user_profiles")
    fun getAllUserProfiles(): Flow<List<UserProfileEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfileEntity)
    
    @Delete
    suspend fun deleteUserProfile(profile: UserProfileEntity)
    
    @Query("DELETE FROM user_profiles WHERE provider = :provider")
    suspend fun deleteUserProfileByProvider(provider: String)
}
