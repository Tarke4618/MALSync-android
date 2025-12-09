package com.malsync.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.malsync.android.data.local.entity.MappingEntity

@Dao
interface MappingDao {
    @Query("SELECT * FROM id_mappings WHERE malId = :malId")
    suspend fun getMappingByMalId(malId: String): MappingEntity?
    
    // We can add queries for other IDs if we index them or are okay with slower scans. 
    // For now, assume MAL ID is the primary key we map FROM.
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMapping(mapping: MappingEntity)
}
