package com.example.rickandmorty.data.locations.dto

data class ApiResponseLocations(
    val info: InfoDto,
    val results: List<LocationsDto>
) {

    data class InfoDto(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )


    data class LocationsDto(
        val id: Int,
        val name: String,
        val type: String,
        val dimension: String,
        val residents: List<String>,
        val url: String,
        val created: String
    )


}