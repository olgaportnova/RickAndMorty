package com.example.rickandmorty.data.episodes.dto

import com.google.gson.annotations.SerializedName


data class ApiResponseEpisodes(
    val info: InfoDto,
    val results: List<EpisodesDto>
) {

    data class InfoDto(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )


    data class EpisodesDto(
        val id: Int,
        val name: String,
        @SerializedName("air_date")
        val airDate: String,
        val episode: String,
        val characters: List<String>,
        val url: String,
        val created: String
    )

}

