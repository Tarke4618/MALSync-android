package com.malsync.android.data.di

import com.malsync.android.data.repository.AnimeRepositoryImpl
import com.malsync.android.data.repository.AuthRepositoryImpl
import com.malsync.android.data.repository.StreamingSiteRepositoryImpl
import com.malsync.android.domain.repository.AnimeRepository
import com.malsync.android.domain.repository.AuthRepository
import com.malsync.android.domain.repository.StreamingSiteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAnimeRepository(
        animeRepositoryImpl: AnimeRepositoryImpl
    ): AnimeRepository
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindStreamingSiteRepository(
        streamingSiteRepositoryImpl: StreamingSiteRepositoryImpl
    ): StreamingSiteRepository
}
