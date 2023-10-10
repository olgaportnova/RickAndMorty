package com.example.rickandmorty.presentation.characters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickandmorty.data.characters.utils.CharacterConverter
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status
import com.example.rickandmorty.domain.episodes.EpisodeInteractor
import com.example.rickandmorty.presentation.characters.utils.CharacterState
import com.example.rickandmorty.presentation.characters.utils.SearchRequestParams
import com.example.rickandmorty.utils.SearchCategories
import com.example.rickandmorty.utils.SearchCategoriesCharacters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CharactersViewModel (
    private val characterConverter: CharacterConverter,
    private val characterInteractor: CharacterInteractor
): ViewModel() {

    private val _state = MutableStateFlow(CharacterState())
    val state: StateFlow<CharacterState> get() = _state

    private val _genderStateFlow = MutableStateFlow<Gender>(Gender.NONE)
    private val genderStateFlow: StateFlow<Gender> = _genderStateFlow

    private val _statusStateFlow = MutableStateFlow<Status>(Status.NONE)
    private val statusStateFlow: StateFlow<Status> = _statusStateFlow

    private val _nameForSearch = MutableStateFlow<String?>("")
    private val nameForSearch: StateFlow<String?> = _nameForSearch

    private val _speciesForSearch = MutableStateFlow<String?>("")
    private val speciesForSearch: StateFlow<String?> = _speciesForSearch

    private val _typeForSearch = MutableStateFlow<String?>("")
    private val typeForSearch: StateFlow<String?> = _typeForSearch

    private val _charactersSearchResult = MutableStateFlow<List<Characters>>(emptyList())
    val charactersSearchResult: StateFlow<List<Characters>> get() = _charactersSearchResult

    init {
        viewModelScope.launch {
            getListData().collect { it ->
                _state.value = _state.value.copy(
                    characters = it
                )
            }
        }
    }

    // requests to API
    fun getListData(): Flow<PagingData<Characters>> {
        return combine(genderStateFlow, statusStateFlow, nameForSearch, speciesForSearch, typeForSearch) { gender, status, name, species, type  ->
            SearchRequestParams(gender = gender, status = status, name = name, species = species, type = type)
        }.flatMapLatest { params ->
            characterInteractor.getCharacters(params.gender, params.status, params.name, params.species, params.type )
                .flow
                .map { pagingData -> pagingData.map { characterConverter.map(it) } }
                .cachedIn(viewModelScope)
        }
    }
    suspend fun getCharacter(id: Int) : Characters? {
        return withContext(Dispatchers.IO) {
            characterInteractor.getCharacterByIdFromApi(id)
        }
    }
    suspend fun getMultipleCharacters(listOfId: List<Int>) {
        withContext(Dispatchers.IO) {
            val result = characterInteractor.getMultipleCharactersFromApi(listOfId)

            if (result != null) {
                _charactersSearchResult.value= result
            } else {
                _charactersSearchResult.value= emptyList()
            }
        }
    }

    // requests to local DB
    suspend fun getMultipleCharactersFromDb(listOfId: List<Int>) {
        withContext(Dispatchers.IO) {
            val result = characterInteractor.getMultipleCharactersFromDb(listOfId)
            if (result != null) {
                _charactersSearchResult.value= result
            } else {
                _charactersSearchResult.value= emptyList()
            }
        }
    }
    suspend fun getCharacterFromDb(id: Int) : Characters? {
        return withContext(Dispatchers.IO) {
            characterInteractor.getCharacterByIdFromDb(id)
        }
    }

    // search support functions
    fun setStatusState(status: Status) {
        _statusStateFlow.value = status
        _state.value = _state.value.copy(isFilter = status != Status.NONE)
    }
    fun setGenderState(gender: Gender) {
        _genderStateFlow.value = gender
        _state.value = _state.value.copy(isFilter = gender != Gender.NONE)
    }
    fun updateListWithSearch(selectedCategory: SearchCategories, searchText: String) {
        when(selectedCategory) {
            SearchCategoriesCharacters.NAME->_nameForSearch.value = searchText
            SearchCategoriesCharacters.SPECIES->_speciesForSearch.value = searchText
            SearchCategoriesCharacters.TYPE->_typeForSearch.value = searchText
            else -> {}
        }
    }
    fun clearTextSearchField() {
       _nameForSearch.value = ""
       _speciesForSearch.value = ""
        _typeForSearch.value = ""
    }

    }





