package com.malsync.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.malsync.android.data.local.entity.AnimeEntity
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.model.UserAnimeStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime_cache WHERE id = :id")
    fun getAnime(id: String): Flow<AnimeEntity?>

    @Query("SELECT * FROM anime_cache WHERE id = :id")
    suspend fun getAnimeSync(id: String): AnimeEntity?

    @Query("SELECT * FROM anime_cache WHERE id = :id")
    suspend fun getAnimeById(id: String): AnimeEntity?

    @Query("SELECT * FROM anime_cache WHERE provider = :provider")
    fun getAnimeByProvider(provider: SyncProvider): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime_cache WHERE provider = :provider AND userStatus = :status")
    fun getAnimeByProviderAndStatus(provider: SyncProvider, status: UserAnimeStatus): Flow<List<AnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimes(animes: List<AnimeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnime(animeList: List<AnimeEntity>)
    
    @Query("DELETE FROM anime_cache WHERE id = :id")
    suspend fun deleteAnime(id: String)
    
    @Query("DELETE FROM anime_cache WHERE id = :id")
    suspend fun deleteAnimeById(id: String)

    @Query("DELETE FROM anime_cache WHERE provider = :provider")
    suspend fun deleteAllByProvider(provider: SyncProvider)
    
    @Query("DELETE FROM anime_cache")
    suspend fun clearAll()
}
