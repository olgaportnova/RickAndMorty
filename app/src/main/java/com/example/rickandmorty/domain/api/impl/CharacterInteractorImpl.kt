package com.example.rickandmorty.domain.api.impl

import com.example.rickandmorty.data.dto.ApiResponse
import com.example.rickandmorty.domain.api.CharacterInteractor
import com.example.rickandmorty.domain.api.CharacterRepository
import com.example.rickandmorty.presentation.characters.utils.Gender
import com.example.rickandmorty.presentation.characters.utils.Status
import retrofit2.Response

class CharacterInteractorImpl(
    private val characterRepository: CharacterRepository
): CharacterInteractor {
    override suspend fun getCharacters(page: Int, gender: Gender, status: Status): Response<ApiResponse> {
        return characterRepository.getCharactersList(page, gender.title, status.title)
    }
}
