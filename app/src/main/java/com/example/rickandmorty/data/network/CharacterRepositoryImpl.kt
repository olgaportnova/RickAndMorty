package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.dto.ApiResponse
import com.example.rickandmorty.domain.api.CharacterRepository
import com.example.rickandmorty.domain.model.Characters
import com.example.rickandmorty.presentation.characters.utils.Gender
import com.example.rickandmorty.utils.Resource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi
): CharacterRepository {

    override suspend fun getCharactersList(page: Int, gender: String, status: String) = api.getCharacters(page, gender, status)
}

