package com.example.rickandmorty.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rickandmorty.data.characters.db.dao.CharactersDao
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.data.episodes.utils.EpisodeConverter
import com.example.rickandmorty.data.episodes.db.dao.EpisodesDao
import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity
import com.example.rickandmorty.data.locations.db.dao.LocationsDao
import com.example.rickandmorty.data.locations.db.entity.LocationEntity

@Database(
    entities = [CharactersEntity::class, EpisodeEntity::class, LocationEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(EpisodeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
    abstract fun episodeDao(): EpisodesDao
    abstract fun locationDao(): LocationsDao


    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `locations` ( " +
                            "`id` INTEGER PRIMARY KEY NOT NULL, " +
                            "`name` TEXT NOT NULL DEFAULT '', " +
                            "`type` TEXT NOT NULL DEFAULT '', " +
                            "`dimension` TEXT NOT NULL DEFAULT '', " +
                            "`residents` TEXT NOT NULL DEFAULT '', " +
                            "`url` TEXT NOT NULL DEFAULT '', " +
                            "`created` TEXT NOT NULL DEFAULT ''" +
                            ")"
                )
            }
        }
    }

}




