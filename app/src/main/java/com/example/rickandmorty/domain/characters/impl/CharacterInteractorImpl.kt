package com.example.rickandmorty.domain.characters.impl

import androidx.paging.Pager
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.CharacterRepository

class CharacterInteractorImpl(
    private val repository: CharacterRepository
) : CharacterInteractor {
    override fun createPager(): Pager<Int, CharactersEntity> {
        return repository.createPager()
    }
}