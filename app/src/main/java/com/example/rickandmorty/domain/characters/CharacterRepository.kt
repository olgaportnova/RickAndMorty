package com.example.rickandmorty.domain.characters

import androidx.paging.Pager
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity


interface CharacterRepository {
    fun getCharacters(gender: String,
                      status: String,
                      name: String?,
                      species: String?,
                      type: String?): Pager<Int, CharactersEntity>
}
