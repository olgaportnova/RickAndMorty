package com.example.rickandmorty.data.characters.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="characters")
data class CharactersEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "species")
    val species: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "gender")
    val gender: String,
    @Embedded(prefix = "origin_")
    val origin: Origin,
    @Embedded(prefix = "location_")
    val location: Location,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "episode")
    val episode: List<String>,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "created")
    val created: String
)

data class Origin(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "url")
    val url: String
)

data class Location(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "url")
    val url: String
)
