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
import com.example.rickandmorty.utils.SearchCategories

class CharacterViewModel (
    private val interactor: CharacterInteractor,
) : ViewModel() {

    private val selectedGender = MutableLiveData<Gender>()
    private val selectedStatus = MutableLiveData<Status>()
    private val searchName = MutableLiveData<String?>()
    private val searchSpecies = MutableLiveData<String?>()
    private val searchType = MutableLiveData<String?>()


    val loading = MutableLiveData<Boolean>()

    var charactersList = Pager(PagingConfig(2)) {
        CharactersPagingSource(
            interactor,
            selectedGender,
            selectedStatus,
            searchName,
            searchSpecies,
            searchType
        )
    }.flow.cachedIn(viewModelScope)

    fun updateCharactersListWithFilters(gender: Gender, status: Status) {
        selectedGender.value = gender
        selectedStatus.value = status
        val updatedCharactersList = Pager(PagingConfig(2)) {
            CharactersPagingSource(
                interactor,
                selectedGender,
                selectedStatus,
                searchName,
                searchSpecies,
                searchType
            )
        }.flow.cachedIn(viewModelScope)

        charactersList = updatedCharactersList
    }

    fun updateCharactersListWithSearch(selectedCategory: String?, searchText: String?) {
        when (selectedCategory) {
            SearchCategories.NAME -> {
                searchName.value = searchText
            }

            SearchCategories.SPECIES -> {
                searchSpecies.value = searchText
            }

            SearchCategories.TYPE -> {
                searchType.value = searchText
            }

            else -> {
                searchName.value = null
                searchSpecies.value = null
                searchType.value = null
            }
        }
        val updatedCharactersList = Pager(PagingConfig(2)) {
            CharactersPagingSource(
                interactor,
                selectedGender,
                selectedStatus,
                searchName,
                searchSpecies,
                searchType
            )
        }.flow.cachedIn(viewModelScope)

        charactersList = updatedCharactersList


    }

}

