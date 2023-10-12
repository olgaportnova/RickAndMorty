package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.characters.dto.ApiResponseCharacters
import com.example.rickandmorty.data.episodes.dto.ApiResponseEpisodes
import com.example.rickandmorty.data.locations.dto.ApiResponseLocations
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    //Сharacters
    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("gender") gender: String,
        @Query("status") status: String,
        @Query("name") name: String?,
        @Query("species") species: String?,
        @Query("type") type: String?
    ): Response<ApiResponseCharacters>
    @GET("/api/character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<ApiResponseCharacters.CharacterDto>
    @GET("/api/character/{ids}")
    suspend fun getMultipleCharacters(@Path("ids") ids: String): List<ApiResponseCharacters.CharacterDto>


    //Episodes
    @GET("api/episode")
    suspend fun getEpisodes(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") episode: String?,
    ): Response<ApiResponseEpisodes>

    @GET("/api/episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Response<ApiResponseEpisodes.EpisodesDto>

    @GET("/api/episode/{ids}")
    suspend fun getMultipleEpisodes(@Path("ids") ids: String): List<ApiResponseEpisodes.EpisodesDto>


    // Locations

    @GET("api/location")
    suspend fun getLocations(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?,
    ): Response<ApiResponseLocations>

    @GET("/api/location/{id}")
    suspend fun getLocation(@Path("id") id: Int): Response<ApiResponseLocations.LocationsDto>

    @GET("/api/location/{ids}")
    suspend fun getMultipleLocations(@Path("ids") ids: String): List<ApiResponseLocations.LocationsDto>



}