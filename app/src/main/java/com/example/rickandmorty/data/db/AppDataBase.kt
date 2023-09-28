package com.example.rickandmorty.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmorty.data.characters.db.dao.CharactersDao
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.data.db.utils.EpisodeConverter

@Database(entities = [CharactersEntity::class], version = 1, exportSchema = false)
@TypeConverters(EpisodeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
}
