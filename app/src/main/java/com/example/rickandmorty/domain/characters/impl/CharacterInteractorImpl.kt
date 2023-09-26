package com.example.rickandmorty.domain.characters.impl

import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.CharacterRepository
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

class CharacterInteractorImpl(
    private val characterRepository: CharacterRepository
) : CharacterInteractor {
    override suspend fun getCharacters(
        page: Int,
        gender: Gender,
        status: Status
    ): List<Characters> {
        return characterRepository.getCharactersList(page, gender.title, status.title)
    }
}
