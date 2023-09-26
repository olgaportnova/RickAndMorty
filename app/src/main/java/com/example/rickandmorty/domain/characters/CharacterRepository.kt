package com.example.rickandmorty.domain.characters

import com.example.rickandmorty.domain.characters.model.Characters

interface CharacterRepository {

    suspend fun getCharactersList(
        page: Int,
        gender: String,
        status: String,
        name:String?,
        species:String?,
        type:String?
        ): List<Characters>
}