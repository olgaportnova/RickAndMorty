package com.example.rickandmorty.domain.characters

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.domain.characters.model.Characters
import kotlinx.coroutines.flow.Flow


interface CharacterRepository {
    fun createPager(): Pager<Int, CharactersEntity>
}
