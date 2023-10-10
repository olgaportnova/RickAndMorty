package com.example.rickandmorty.domain.characters

import androidx.paging.Pager
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.model.Episodes


interface CharacterRepository {
    fun getCharacters(gender: String,
                      status: String,
                      name: String?,
                      species: String?,
                      type: String?): Pager<Int, CharactersEntity>


    suspend fun getCharacterByIdFromApi(id:Int) : Characters?

    suspend fun getMultipleCharactersFromApi(ids: List<Int>): List<Characters>?

    suspend fun getCharacterByIdFromDb(id: Int) : Characters?

    suspend fun getMultipleCharactersFromDb(ids: List<Int>): List<Characters>?
}
