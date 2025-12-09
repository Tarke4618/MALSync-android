package com.malsync.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.malsync.android.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime_cache WHERE id = :id")
    fun getAnime(id: String): Flow<AnimeEntity?>

    @Query("SELECT * FROM anime_cache WHERE id = :id")
    suspend fun getAnimeSync(id: String): AnimeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimes(animes: List<AnimeEntity>)
    
    @Query("DELETE FROM anime_cache WHERE id = :id")
    suspend fun deleteAnime(id: String)
    
    @Query("DELETE FROM anime_cache")
    suspend fun clearAll()
}
