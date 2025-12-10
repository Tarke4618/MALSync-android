package com.malsync.android.data.di

import com.malsync.android.data.repository.AnimeRepositoryImpl
import com.malsync.android.data.repository.AuthRepositoryImpl
import com.malsync.android.data.repository.MappingRepositoryImpl
import com.malsync.android.data.repository.MultiServiceRepositoryImpl
import com.malsync.android.data.repository.StreamingSiteRepositoryImpl
import com.malsync.android.domain.repository.AnimeRepository
import com.malsync.android.domain.repository.AuthRepository
import com.malsync.android.domain.repository.MappingRepository
import com.malsync.android.domain.repository.MultiServiceRepository
import com.malsync.android.domain.repository.StreamingSiteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMappingRepository(
        mappingRepositoryImpl: MappingRepositoryImpl
    ): MappingRepository

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindMultiServiceRepository(
        multiServiceRepositoryImpl: MultiServiceRepositoryImpl
    ): MultiServiceRepository

    @Binds
    abstract fun bindAnimeRepository(
        animeRepositoryImpl: AnimeRepositoryImpl
    ): AnimeRepository

    @Binds
    abstract fun bindStreamingSiteRepository(
        streamingSiteRepositoryImpl: StreamingSiteRepositoryImpl
    ): StreamingSiteRepository
}
