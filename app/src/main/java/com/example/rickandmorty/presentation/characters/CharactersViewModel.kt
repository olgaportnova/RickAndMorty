package com.example.rickandmorty.presentation.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.rickandmorty.domain.api.CharacterRepository
import com.example.rickandmorty.paging.CharactersPagingSource

class CharacterViewModel (
    private val repository: CharacterRepository
) : ViewModel() {

    val loading = MutableLiveData<Boolean>()

    val charactersList = Pager(PagingConfig(1)) {
        CharactersPagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}
