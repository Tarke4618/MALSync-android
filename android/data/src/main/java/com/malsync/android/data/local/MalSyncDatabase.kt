package com.malsync.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.malsync.android.data.local.converter.Converters
import com.malsync.android.data.local.dao.*
import com.malsync.android.data.local.entity.*

@Database(
    entities = [
        AnimeEntity::class,
        MangaEntity::class,
        AuthTokenEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MalSyncDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun mangaDao(): MangaDao
    abstract fun authTokenDao(): AuthTokenDao
    abstract fun userProfileDao(): UserProfileDao
}
