package com.example.rickandmorty.data.db.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections

class EpisodeConverter{
    private val gson = Gson()

    @TypeConverter
    fun fromStringToList(data: String?): List<String> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromListToString(someObjects: List<String>): String {
        return gson.toJson(someObjects)
    }
}
