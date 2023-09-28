package com.example.rickandmorty.domain.characters

import androidx.paging.PagingData
import com.example.rickandmorty.domain.characters.model.Characters
import kotlinx.coroutines.flow.Flow


interface CharacterRepository {
    fun getCharactersPaged(gender: String?): Flow<PagingData<Characters>>
}
