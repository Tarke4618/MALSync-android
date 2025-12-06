package com.malsync.android.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Type converters for Room database
 */
class Converters {
    private val gson = Gson()
    
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
    
    @TypeConverter
    fun fromStringMap(value: Map<String, String>?): String {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toStringMap(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, mapType) ?: emptyMap()
    }
}
