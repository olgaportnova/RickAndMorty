package com.example.rickandmorty.data.episodes.dto



data class EpisodesDto(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
    )


