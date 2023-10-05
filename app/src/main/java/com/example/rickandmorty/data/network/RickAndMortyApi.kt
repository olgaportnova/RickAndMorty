package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.characters.dto.ApiResponseCharacters
import com.example.rickandmorty.data.episodes.dto.ApiResponseEpisodes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
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


    @GET("api/episode")
    suspend fun getEpisodes(
        @Query("name") name: String?,
        @Query("episode") episode: String?,
    ): Response<ApiResponseEpisodes>


    @GET("/api/episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Response<ApiResponseEpisodes.EpisodesDto>


    @GET("/api/episode/{ids}")
    suspend fun getMultipleEpisodes(@Path("ids") ids: String): List<ApiResponseEpisodes.EpisodesDto>




}