package com.example.rickandmorty.domain.characters.impl

import androidx.paging.Pager
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.CharacterRepository
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

class CharacterInteractorImpl(
    private val repository: CharacterRepository
) : CharacterInteractor {
    override fun getCharacters(
        gender: Gender?,
        status: Status?,
        name: String?,
        species: String?,
        type: String?
    ): Pager<Int, CharactersEntity> {
        val genderString = gender?.title ?: ""
        val statusString = status?.title ?: ""
        return repository.getCharacters(genderString, statusString, name, species, type)
    }

    override suspend fun getCharacterByIdFromApi(id:Int) : Characters? {
       return repository.getCharacterByIdFromApi(id)
    }

    override suspend fun getMultipleCharactersFromApi(ids: List<Int>): List<Characters>?{
        return repository.getMultipleCharactersFromApi(ids)
    }

    override suspend fun getCharacterByIdFromDb(id:Int) : Characters? {
        return repository.getCharacterByIdFromDb(id)
    }

    override suspend fun getMultipleCharactersFromDb(ids: List<Int>): List<Characters>? {
        return repository.getMultipleCharactersFromDb(ids)
    }


}