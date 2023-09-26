package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.characters.dto.ApiResponseCharacters
import retrofit2.Response

import retrofit2.http.GET
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
}