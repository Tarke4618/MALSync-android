package com.malsync.android.data.di

import com.malsync.android.data.repository.MappingRepositoryImpl
import com.malsync.android.domain.repository.MappingRepository
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
}
