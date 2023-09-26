package com.example.rickandmorty.presentation.characters.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.paging.characters.CharactersPagingSource
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

class CharacterViewModel (
    private val interactor: CharacterInteractor,
) : ViewModel() {

    private val selectedGender = MutableLiveData<Gender>()
    private val selectedStatus = MutableLiveData<Status>()


    val loading = MutableLiveData<Boolean>()

    var charactersList = Pager(PagingConfig(1)) {
        CharactersPagingSource(interactor, selectedGender, selectedStatus)
    }.flow.cachedIn(viewModelScope)

    fun updateCharactersListWithFilters(gender: Gender, status: Status) {
        selectedGender.value = gender
        selectedStatus.value = status
        val updatedCharactersList = Pager(PagingConfig(1)) {
            CharactersPagingSource(interactor, selectedGender, selectedStatus)
        }.flow.cachedIn(viewModelScope)

        charactersList = updatedCharactersList
    }

}
