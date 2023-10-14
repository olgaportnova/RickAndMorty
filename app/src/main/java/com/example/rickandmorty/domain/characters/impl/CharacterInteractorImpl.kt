package com.example.rickandmorty.domain.characters.impl

import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmorty.data.characters.utils.CharacterConverter
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.CharacterRepository
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterInteractorImpl(
    private val repository: CharacterRepository,
    private val converter: CharacterConverter
) : CharacterInteractor {
    override fun getCharacters(
        gender: Gender?,
        status: Status?,
        name: String?,
        species: String?,
        type: String?
    ): Flow<PagingData<Characters>> {
        val genderString = gender?.title ?: ""
        val statusString = status?.title ?: ""
        return repository
            .getCharacters(genderString, statusString, name, species, type)
            .flow
            .map { pagingData -> pagingData.map { converter.map(it) } }

    }

    override suspend fun getCharacterByIdFromApi(id: Int): Characters? {
        return repository.getCharacterByIdFromApi(id)
    }

    override suspend fun getMultipleCharactersFromApi(ids: List<Int>): List<Characters>? {
        return repository.getMultipleCharactersFromApi(ids)
    }

    override suspend fun getCharacterByIdFromDb(id: Int): Characters? {
        return repository.getCharacterByIdFromDb(id)
    }

    override suspend fun getMultipleCharactersFromDb(ids: List<Int>): List<Characters>? {
        return repository.getMultipleCharactersFromDb(ids)
    }


}