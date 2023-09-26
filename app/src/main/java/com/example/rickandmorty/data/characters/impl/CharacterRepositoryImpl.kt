package com.example.rickandmorty.data.characters.impl

import com.example.rickandmorty.data.characters.utils.CharacterConverter
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.example.rickandmorty.domain.characters.CharacterRepository
import com.example.rickandmorty.domain.characters.model.Characters

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi,
    private val characterConverter: CharacterConverter
) : CharacterRepository {

    override suspend fun getCharactersList(
        page: Int,
        gender: String,
        status: String,
        name: String?,
        species: String?,
        type: String?
    ): List<Characters> {
        val response = api.getCharacters(page, gender, status, name, species, type)
        val characterDtos = response.body()?.results ?: emptyList()
        val charactersList = mutableListOf<Characters>()

        for (characterDto in characterDtos) {
            val character =
                characterConverter.map(characterDto)
            charactersList.add(character)
        }

        return charactersList
    }
}
