package com.malsync.android.data.local.dao

import androidx.room.*
import com.malsync.android.data.local.entity.MangaEntity
import kotlinx.coroutines.flow.Flow

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
