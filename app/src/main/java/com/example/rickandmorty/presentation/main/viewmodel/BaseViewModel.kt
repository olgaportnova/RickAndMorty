package com.example.rickandmorty.presentation.main.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel(
    private val characterInteractor: CharacterInteractor
) : ViewModel() {

    protected val _isNetworkAvailable = MutableLiveData<Boolean>(false)
    val isNetworkAvailable: LiveData<Boolean> = _isNetworkAvailable

    protected val _charactersIds = MutableLiveData<List<Int>>(emptyList())
    val charactersIds: LiveData<List<Int>> = _charactersIds

    protected val _charactersSearchResult = MutableStateFlow<List<Characters>>(emptyList())
    val charactersSearchResult: StateFlow<List<Characters>> get() = _charactersSearchResult


    fun checkNetworkAvailability(context: Context) {
        viewModelScope.launch {
            _isNetworkAvailable.value = NetworkUtils.isNetworkAvailable(context)
        }
    }
    fun loadCharacters(charactersIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isNetworkAvailable.value == true) {
                val result = characterInteractor.getMultipleCharactersFromApi(charactersIds)
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        _charactersSearchResult.value = result
                    } else {
                        _charactersSearchResult.value = emptyList()
                    }
                }
            } else {
                val result = characterInteractor.getMultipleCharactersFromDb(charactersIds)
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        _charactersSearchResult.value = result
                    } else {
                        _charactersSearchResult.value = emptyList()
                    }
                }
            }
        }
    }

}
