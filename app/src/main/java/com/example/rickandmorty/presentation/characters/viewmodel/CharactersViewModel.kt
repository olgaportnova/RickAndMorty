package com.example.rickandmorty.presentation.characters.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickandmorty.data.characters.utils.CharacterConverter
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.CharacterRepository
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class CharactersViewModel (
    private val characterConverter: CharacterConverter,
    private val characterInteractor: CharacterInteractor
): ViewModel() {
    private val selectedGender = MutableLiveData<Gender>()

    val charactersPagingData: Flow<PagingData<Characters>>


    init {
        charactersPagingData = characterInteractor.createPager()
            .flow
            .map { pagingData ->
                pagingData.map { characterConverter.map(it) }
            }
            .cachedIn(viewModelScope)
    }

     }














