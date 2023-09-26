package com.example.rickandmorty.domain.api

import com.example.rickandmorty.data.dto.ApiResponse
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.example.rickandmorty.domain.model.Characters
import com.example.rickandmorty.presentation.characters.utils.Gender
import com.example.rickandmorty.utils.Resource
import retrofit2.Response

interface CharacterRepository {

  //  suspend fun getCharacters(page: Int): Resource<ApiResponse>

    suspend fun getCharactersList(page: Int, gender: String, status: String) : Response<ApiResponse>
}