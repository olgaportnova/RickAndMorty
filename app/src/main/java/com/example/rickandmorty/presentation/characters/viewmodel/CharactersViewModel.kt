package com.example.rickandmorty.presentation.characters.viewmodel

import android.util.Log
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
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.characters.utils.CharacterState
import com.example.rickandmorty.presentation.characters.utils.SearchCategories
import com.example.rickandmorty.presentation.characters.utils.SearchRequestParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private val characterInteractor: CharacterInteractor,
    private val episodeInteractor: EpisodeInteractor
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



    init {
        viewModelScope.launch {
            getListData().collect { it ->
                _state.value = _state.value.copy(
                    characters = it
                )
            }
        }
    }



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
    fun setStatusState(status: Status) {
        _statusStateFlow.value = status
        _state.value = _state.value.copy(isFilter = status != Status.NONE)
    }
    fun setGenderState(gender: Gender) {
        _genderStateFlow.value = gender
        _state.value = _state.value.copy(isFilter = gender != Gender.NONE)
    }
    fun updateCharactersListWithSearch(selectedCategory: SearchCategories, searchText: String) {
        when(selectedCategory) {
            SearchCategories.NAME->_nameForSearch.value = searchText
            SearchCategories.SPECIES->_speciesForSearch.value = searchText
            SearchCategories.TYPE->_typeForSearch.value = searchText
            else -> {}
        }
    }
    fun сlearTextSearchField() {
       _nameForSearch.value = ""
       _speciesForSearch.value = ""
        _typeForSearch.value = ""
    }

    suspend fun getCharacters(listOfId: List<Int>) : List<Characters> {
        return withContext(Dispatchers.Default) {
            val deferredList = listOfId.map { id ->
                async {
                    characterInteractor.getCharacterById(id)
                }
            }
            Log.d("TAG123", "deferredList $deferredList")
            val resultList = deferredList.awaitAll()
            Log.d("TAG123", "resultList - $resultList")

            val filteredList = resultList.filterNotNull()
            Log.d("TAG123", "filteredList - $filteredList")

            filteredList
        }
    }

    suspend fun getCharacter(id: Int) : Characters? {
        return withContext(Dispatchers.IO) {
            characterInteractor.getCharacterById(id)
                }
        }
    }





