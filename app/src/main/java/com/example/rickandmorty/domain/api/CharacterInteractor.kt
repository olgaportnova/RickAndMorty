package com.example.rickandmorty.domain.api

import com.example.rickandmorty.data.dto.ApiResponse
import com.example.rickandmorty.domain.model.Characters
import com.example.rickandmorty.presentation.characters.utils.Gender
import com.example.rickandmorty.presentation.characters.utils.Status
import com.example.rickandmorty.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CharacterInteractor {

    suspend fun getCharacters(page: Int, gender: Gender, status: Status): Response<ApiResponse>
}