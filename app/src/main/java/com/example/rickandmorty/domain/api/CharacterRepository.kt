package com.example.rickandmorty.domain.api

import com.example.rickandmorty.data.dto.ApiResponse
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.example.rickandmorty.domain.model.Characters
import com.example.rickandmorty.utils.Resource

interface CharacterRepository {

    suspend fun getCharacters(page: Int): Resource<List<Characters>>
}