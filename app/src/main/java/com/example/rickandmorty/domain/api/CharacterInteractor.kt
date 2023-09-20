package com.example.rickandmorty.domain.api

import com.example.rickandmorty.data.dto.ApiResponse
import com.example.rickandmorty.domain.model.Characters
import com.example.rickandmorty.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CharacterInteractor {

    suspend fun getCharacters(page: Int): Flow<Pair<List<Characters>?, String?>>
}