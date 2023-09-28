package com.example.rickandmorty.presentation.characters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.data.characters.utils.CharacterConverter
import kotlinx.coroutines.flow.map

//class CharacterViewModel (
//    private val interactor: CharacterInteractor,
//) : ViewModel() {
//
//    private val selectedGender = MutableLiveData<Gender>()
//    private val selectedStatus = MutableLiveData<Status>()
//    private val searchName = MutableLiveData<String?>()
//    private val searchSpecies = MutableLiveData<String?>()
//    private val searchType = MutableLiveData<String?>()
//
//
//    val loading = MutableLiveData<Boolean>()
//
//    var charactersList = Pager(PagingConfig(2)) {
//        CharactersPagingSource(
//            interactor,
//            selectedGender,
//            selectedStatus,
//            searchName,
//            searchSpecies,
//            searchType
//        )
//    }.flow.cachedIn(viewModelScope)
//
//    fun updateCharactersListWithFilters(gender: Gender, status: Status) {
//        selectedGender.value = gender
//        selectedStatus.value = status
//        val updatedCharactersList = Pager(PagingConfig(2)) {
//            CharactersPagingSource(
//                interactor,
//                selectedGender,
//                selectedStatus,
//                searchName,
//                searchSpecies,
//                searchType
//            )
//        }.flow.cachedIn(viewModelScope)
//
//        charactersList = updatedCharactersList
//    }
//
//    fun updateCharactersListWithSearch(selectedCategory: String?, searchText: String?) {
//        when (selectedCategory) {
//            SearchCategories.NAME -> {
//                searchName.value = searchText
//            }
//
//            SearchCategories.SPECIES -> {
//                searchSpecies.value = searchText
//            }
//
//            SearchCategories.TYPE -> {
//                searchType.value = searchText
//            }
//
//            else -> {
//                searchName.value = null
//                searchSpecies.value = null
//                searchType.value = null
//            }
//        }
//        val updatedCharactersList = Pager(PagingConfig(2)) {
//            CharactersPagingSource(
//                interactor,
//                selectedGender,
//                selectedStatus,
//                searchName,
//                searchSpecies,
//                searchType
//            )
//        }.flow.cachedIn(viewModelScope)
//
//        charactersList = updatedCharactersList
//
//
//    }
//
//}
//

//class CharactersViewModel(
//    private val characterInteractor: CharacterInteractor
//): ViewModel() {
//
//    private val selectedGender = MutableLiveData<Gender>()
//
//
//    var charactersPagingData = selectedGender.asFlow()
//        .flatMapLatest { gender ->
//            characterInteractor.getCharactersPaged(selectedGender.value)
//        }
//        .cachedIn(viewModelScope)
//
//    val a = charactersPagingData
//
//
//    fun selectGender(gender: Gender) {
//        selectedGender.value = gender
//        charactersPagingData = characterInteractor.getCharactersPaged(gender)
//
//    } }

class CharactersViewModel (
    private val characterConverter: CharacterConverter,
    pager: Pager<Int,CharactersEntity>
): ViewModel() {

    val charactersPagingData = pager
        .flow
        .map{ pagingData ->
            pagingData.map {characterConverter.map(it)}
        }.cachedIn(viewModelScope)

}













