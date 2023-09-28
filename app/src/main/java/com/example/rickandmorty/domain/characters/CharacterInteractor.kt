package com.example.rickandmorty.domain.characters

import androidx.paging.Pager
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity

interface CharacterInteractor {
    fun createPager(): Pager<Int, CharactersEntity>
}
