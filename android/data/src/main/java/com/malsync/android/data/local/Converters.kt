package com.malsync.android.data.local

import androidx.room.TypeConverter
import com.malsync.android.domain.model.AnimeStatus
import com.malsync.android.domain.model.AnimeType
import com.malsync.android.domain.model.SyncProvider
import com.malsync.android.domain.model.UserAnimeStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value ?: emptyList<String>())
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromProviderMap(value: Map<SyncProvider, String>?): String {
        return gson.toJson(value ?: emptyMap<SyncProvider, String>())
    }

    @TypeConverter
    fun toProviderMap(value: String): Map<SyncProvider, String> {
        val type = object : TypeToken<Map<SyncProvider, String>>() {}.type
        return gson.fromJson(value, type) ?: emptyMap()
    }
    
    @TypeConverter
    fun fromAnimeType(value: AnimeType): String = value.name

    @TypeConverter
    fun toAnimeType(value: String): AnimeType = runCatching { AnimeType.valueOf(value) }.getOrDefault(AnimeType.UNKNOWN)

    @TypeConverter
    fun fromAnimeStatus(value: AnimeStatus): String = value.name

    @TypeConverter
    fun toAnimeStatus(value: String): AnimeStatus = runCatching { AnimeStatus.valueOf(value) }.getOrDefault(AnimeStatus.UNKNOWN)
    
    @TypeConverter
    fun fromUserAnimeStatus(value: UserAnimeStatus): String = value.name

    @TypeConverter
    fun toUserAnimeStatus(value: String): UserAnimeStatus = runCatching { UserAnimeStatus.valueOf(value) }.getOrDefault(UserAnimeStatus.WATCHING)

    @TypeConverter
    fun fromSyncProvider(value: SyncProvider): String = value.name

    @TypeConverter
    fun toSyncProvider(value: String): SyncProvider = runCatching { SyncProvider.valueOf(value) }.getOrDefault(SyncProvider.MYANIMELIST)
}
