package com.example.rickandmorty.domain.characters

import androidx.paging.Pager
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

interface CharacterInteractor {
    fun getCharacters(gender: Gender?,
                      status: Status?,
                      name:String?,
                      species:String?,
                      type:String?
    ): Pager<Int, CharactersEntity>

    suspend fun getCharacterById(id:Int): Characters?
}
