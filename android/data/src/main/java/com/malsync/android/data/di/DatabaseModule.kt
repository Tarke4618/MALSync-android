package com.malsync.android.data.di

import android.content.Context
import androidx.room.Room
import com.malsync.android.data.local.MalSyncDatabase
import com.malsync.android.data.local.dao.*
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
    fun provideMalSyncDatabase(@ApplicationContext context: Context): MalSyncDatabase {
        return Room.databaseBuilder(
            context,
            MalSyncDatabase::class.java,
            "malsync_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideAnimeDao(database: MalSyncDatabase): AnimeDao {
        return database.animeDao()
    }
    
    @Provides
    fun provideMangaDao(database: MalSyncDatabase): MangaDao {
        return database.mangaDao()
    }
    
    @Provides
    fun provideAuthTokenDao(database: MalSyncDatabase): AuthTokenDao {
        return database.authTokenDao()
    }
    
    @Provides
    fun provideUserProfileDao(database: MalSyncDatabase): UserProfileDao {
        return database.userProfileDao()
    }
}
