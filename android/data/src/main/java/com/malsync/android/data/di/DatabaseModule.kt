package com.malsync.android.data.di

import android.content.Context
import androidx.room.Room
import com.malsync.android.data.local.MALSyncDatabase
import com.malsync.android.data.local.dao.AnimeDao
import com.malsync.android.data.local.dao.MappingDao
import com.malsync.android.data.local.dao.UserListEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MALSyncDatabase {
        return Room.databaseBuilder(
            context,
            MALSyncDatabase::class.java,
            "malsync.db"
        )
        .fallbackToDestructiveMigration() // For development only
        .build()
    }

    @Provides
    fun provideAnimeDao(database: MALSyncDatabase): AnimeDao = database.animeDao()

    @Provides
    fun provideUserListEntryDao(database: MALSyncDatabase): UserListEntryDao = database.userListEntryDao()
    
    @Provides
    fun provideMappingDao(database: MALSyncDatabase): MappingDao = database.mappingDao()
}
