package com.malsync.android.data.local.dao

import androidx.room.*
import com.malsync.android.data.local.entity.AnimeEntity
import com.malsync.android.data.local.entity.MangaEntity
import com.malsync.android.data.local.entity.AuthTokenEntity
import com.malsync.android.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime WHERE provider = :provider")
    fun getAnimeByProvider(provider: String): Flow<List<AnimeEntity>>
    
    @Query("SELECT * FROM anime WHERE provider = :provider AND userStatus = :status")
    fun getAnimeByProviderAndStatus(provider: String, status: String): Flow<List<AnimeEntity>>
    
    @Query("SELECT * FROM anime WHERE id = :id LIMIT 1")
    suspend fun getAnimeById(id: String): AnimeEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnime(animeList: List<AnimeEntity>)
    
    @Delete
    suspend fun deleteAnime(anime: AnimeEntity)
    
    @Query("DELETE FROM anime WHERE id = :id")
    suspend fun deleteAnimeById(id: String)
    
    @Query("DELETE FROM anime WHERE provider = :provider")
    suspend fun deleteAllByProvider(provider: String)
    
    @Query("SELECT * FROM anime WHERE title LIKE '%' || :query || '%' OR englishTitle LIKE '%' || :query || '%'")
    suspend fun searchAnime(query: String): List<AnimeEntity>
}

@Dao
interface MangaDao {
    @Query("SELECT * FROM manga WHERE provider = :provider")
    fun getMangaByProvider(provider: String): Flow<List<MangaEntity>>
    
    @Query("SELECT * FROM manga WHERE provider = :provider AND userStatus = :status")
    fun getMangaByProviderAndStatus(provider: String, status: String): Flow<List<MangaEntity>>
    
    @Query("SELECT * FROM manga WHERE id = :id LIMIT 1")
    suspend fun getMangaById(id: String): MangaEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManga(manga: MangaEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllManga(mangaList: List<MangaEntity>)
    
    @Delete
    suspend fun deleteManga(manga: MangaEntity)
    
    @Query("DELETE FROM manga WHERE id = :id")
    suspend fun deleteMangaById(id: String)
    
    @Query("DELETE FROM manga WHERE provider = :provider")
    suspend fun deleteAllByProvider(provider: String)
}

@Dao
interface AuthTokenDao {
    @Query("SELECT * FROM auth_tokens WHERE provider = :provider LIMIT 1")
    suspend fun getAuthToken(provider: String): AuthTokenEntity?
    
    @Query("SELECT * FROM auth_tokens")
    fun getAllAuthTokens(): Flow<List<AuthTokenEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthToken(token: AuthTokenEntity)
    
    @Delete
    suspend fun deleteAuthToken(token: AuthTokenEntity)
    
    @Query("DELETE FROM auth_tokens WHERE provider = :provider")
    suspend fun deleteAuthTokenByProvider(provider: String)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE provider = :provider LIMIT 1")
    suspend fun getUserProfile(provider: String): UserProfileEntity?
    
    @Query("SELECT * FROM user_profiles")
    fun getAllUserProfiles(): Flow<List<UserProfileEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfileEntity)
    
    @Delete
    suspend fun deleteUserProfile(profile: UserProfileEntity)
    
    @Query("DELETE FROM user_profiles WHERE provider = :provider")
    suspend fun deleteUserProfileByProvider(provider: String)
}
