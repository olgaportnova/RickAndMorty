package com.example.rickandmorty.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rickandmorty.data.characters.db.dao.CharactersDao
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.data.db.utils.EpisodeConverter
import com.example.rickandmorty.data.episodes.db.dao.EpisodesDao
import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity

@Database(entities = [CharactersEntity::class,EpisodeEntity::class], version = 2, exportSchema = false)
@TypeConverters(EpisodeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
    abstract fun episodeDao(): EpisodesDao

    companion object {

            val MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // Создать таблицу episodes, если она не существует
                    database.execSQL("CREATE TABLE IF NOT EXISTS `episodes` ( " +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`name` TEXT NOT NULL DEFAULT '', " +
                            "`air_date` TEXT NOT NULL DEFAULT '', " +
                            "`episode` TEXT NOT NULL DEFAULT '', " +
                            "`characters` TEXT NOT NULL DEFAULT '', " +
                            "`url` TEXT NOT NULL DEFAULT '', " +
                            "`created` TEXT NOT NULL DEFAULT ''" +
                            ")")
                }
            }
        }

    }




