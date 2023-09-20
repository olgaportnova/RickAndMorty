package com.example.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.api.CharacterInteractor
import com.example.rickandmorty.domain.model.Characters
import com.example.rickandmorty.utils.Resource
import kotlinx.coroutines.launch

class CharacterViewModel(private val characterInteractor: CharacterInteractor) : ViewModel() {

    private val _characters = MutableLiveData<Resource<List<Characters>>>()
    val characters: LiveData<Resource<List<Characters>>> get() = _characters

    private var currentPage: Int = 1
    var hasMorePages = true

    init {
        fetchCharacters()
    }

    fun fetchCharacters() {
        _characters.postValue(Resource.Loading())

        viewModelScope.launch {
            characterInteractor.getCharacters(currentPage)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(data: List<Characters>?, errorMessage: String?) {
        if (!data.isNullOrEmpty()) {
            val currentData = (_characters.value?.data ?: emptyList())
            val combinedList = currentData + data

            _characters.postValue(Resource.Success(combinedList))
            currentPage++
        } else if (errorMessage != null) {
            _characters.postValue(Resource.Error(errorMessage))
        }
    }
}
