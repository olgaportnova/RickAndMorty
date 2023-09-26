package com.example.rickandmorty.domain.characters

import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

interface CharacterInteractor {

    suspend fun getCharacters(
        page: Int,
        gender: Gender,
        status: Status,
        name: String?,
        species: String?,
        type: String?
    ): List<Characters>
}