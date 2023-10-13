package com.example.rickandmorty.data.episodes.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episodes")
data class EpisodeEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "air_date")
    val airDate: String,
    @ColumnInfo(name = "episode")
    val episode: String,
    @ColumnInfo(name = "characters")
    val characters: List<String>,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "created")
    val created: String,

    )

