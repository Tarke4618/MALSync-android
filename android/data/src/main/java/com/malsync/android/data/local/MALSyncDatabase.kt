package com.malsync.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.malsync.android.data.local.dao.AnimeDao
import com.malsync.android.data.local.dao.MappingDao
import com.malsync.android.data.local.dao.UserListEntryDao
import com.malsync.android.data.local.entity.AnimeEntity
import com.malsync.android.data.local.entity.MappingEntity
import com.malsync.android.data.local.entity.UserListEntryEntity

@Database(
    entities = [
        AnimeEntity::class,
        UserListEntryEntity::class,
        MappingEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MALSyncDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun userListEntryDao(): UserListEntryDao
    abstract fun mappingDao(): MappingDao
}
