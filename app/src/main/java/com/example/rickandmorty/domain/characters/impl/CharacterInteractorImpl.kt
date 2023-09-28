package com.example.rickandmorty.domain.characters.impl

import androidx.paging.Pager
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.CharacterRepository
import com.example.rickandmorty.domain.characters.model.utils.Gender

class CharacterInteractorImpl(
    private val repository: CharacterRepository
) : CharacterInteractor {
    override fun createPager(gender: Gender?): Pager<Int, CharactersEntity> {
        val genderString = gender?.title ?: ""
        return repository.createPager(genderString)
    }
}