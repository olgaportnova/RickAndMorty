package com.example.rickandmorty.domain.characters

import androidx.paging.Pager
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

interface CharacterInteractor {
    fun createPager(gender: Gender?): Pager<Int, CharactersEntity>
}
