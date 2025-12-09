package com.malsync.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malsync.android.domain.model.SyncProvider

@Entity(tableName = "id_mappings")
data class MappingEntity(
    @PrimaryKey val malId: String,
    val anilistId: String? = null,
    val kitsuId: String? = null,
    val simklId: String? = null,
    val shikimoriId: String? = null
)
