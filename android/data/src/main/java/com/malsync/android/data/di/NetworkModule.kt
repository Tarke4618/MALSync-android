package com.malsync.android.data.di

import android.content.Context
import com.malsync.android.data.remote.api.*
import com.malsync.android.data.remote.interceptor.AuthInterceptor
import com.malsync.android.domain.model.SyncProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MalRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AniListRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KitsuRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SimklRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    @Provides
    @Singleton
    fun provideAuthInterceptor(@ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    @MalRetrofit
    fun provideMalRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SyncProvider.MYANIMELIST.apiBaseUrl + "/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @AniListRetrofit
    fun provideAniListRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SyncProvider.ANILIST.apiBaseUrl + "/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @KitsuRetrofit
    fun provideKitsuRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SyncProvider.KITSU.apiBaseUrl + "/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @SimklRetrofit
    fun provideSimklRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SyncProvider.SIMKL.apiBaseUrl + "/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideMalApiService(@MalRetrofit retrofit: Retrofit): MalApiService {
        return retrofit.create(MalApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideAniListApiService(@AniListRetrofit retrofit: Retrofit): AniListApiService {
        return retrofit.create(AniListApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideKitsuApiService(@KitsuRetrofit retrofit: Retrofit): KitsuApiService {
        return retrofit.create(KitsuApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideSimklApiService(@SimklRetrofit retrofit: Retrofit): SimklApiService {
        return retrofit.create(SimklApiService::class.java)
    }
}
