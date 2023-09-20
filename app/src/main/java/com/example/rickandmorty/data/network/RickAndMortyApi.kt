package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.dto.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("api/character")
    suspend fun getCharacters(@Query("page") page: Int): ApiResponse
}
