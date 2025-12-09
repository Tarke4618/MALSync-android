package com.malsync.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.malsync.android.data.local.entity.UserListEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserListEntryDao {
    @Query("SELECT * FROM user_list")
    fun getAllEntries(): Flow<List<UserListEntryEntity>>
    
    @Query("SELECT * FROM user_list")
    suspend fun getAllEntriesSync(): List<UserListEntryEntity>

    @Query("SELECT * FROM user_list WHERE animeId = :animeId")
    fun getEntry(animeId: String): Flow<UserListEntryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: UserListEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<UserListEntryEntity>)
    
    @Query("SELECT * FROM user_list WHERE dirty = 1")
    suspend fun getDirtyEntries(): List<UserListEntryEntity>
    
    @Query("UPDATE user_list SET dirty = 0 WHERE animeId IN (:ids)")
    suspend fun clearDirtyFlags(ids: List<String>)

    @Query("DELETE FROM user_list")
    suspend fun clearAll()
}
