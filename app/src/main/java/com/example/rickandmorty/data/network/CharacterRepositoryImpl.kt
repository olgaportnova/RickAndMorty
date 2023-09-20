package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.dto.ApiResponse
import com.example.rickandmorty.domain.api.CharacterRepository
import com.example.rickandmorty.domain.model.Characters
import com.example.rickandmorty.utils.Resource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi,
    private val characterConverter: CharacterConverter
): CharacterRepository {

    override suspend fun getCharacters(page: Int): Resource<List<Characters>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCharacters(page)
                val characters = response.results.map { characterConverter.map(it) }
                Resource.Success(characters)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }
}

