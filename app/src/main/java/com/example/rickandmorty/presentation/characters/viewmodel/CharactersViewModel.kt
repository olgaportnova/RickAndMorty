package com.example.rickandmorty.presentation.characters.viewmodel

import androidx.lifecycle.MutableLiveData
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
import com.example.rickandmorty.presentation.characters.utils.CharacterState
import com.example.rickandmorty.presentation.characters.utils.SearchRequestParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class CharactersViewModel (
    private val characterConverter: CharacterConverter,
    private val characterInteractor: CharacterInteractor
): ViewModel() {
    private val selectedGender = MutableLiveData<Gender>()


    private val _state = MutableStateFlow(CharacterState())
    val state: StateFlow<CharacterState> get() = _state

    private val _genderStateFlow = MutableStateFlow<Gender>(Gender.NONE)
    val genderStateFlow: StateFlow<Gender> = _genderStateFlow

    private val _statusStateFlow = MutableStateFlow<Status>(Status.NONE)
    val statusStateFlow: StateFlow<Status> = _statusStateFlow




    private val _searchRequestParams = MutableStateFlow(SearchRequestParams())
    val searchRequestParams: StateFlow<SearchRequestParams> = _searchRequestParams



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
        return combine(genderStateFlow, statusStateFlow) { gender, status ->
            SearchRequestParams(gender = gender, status = status)
        }.flatMapLatest { params ->
            characterInteractor.getCharacters(params.gender, params.status, params.name, params.species, params.type)
                .flow
                .map { pagingData -> pagingData.map { characterConverter.map(it) } }
                .cachedIn(viewModelScope)
        }
    }


    fun setStatusState(status: Status) {
        _statusStateFlow.value = status
    }

    fun setGenderState(gender: Gender) {
        _genderStateFlow.value = gender


    }


}
//
//fun checkIfTheFilterHasBeenApplied(): Boolean {
//
//    val statusValue = _state.value.statusState
//    val genderValue = _state.value.genderState
//    val characterName = _state.value.queryCharacterName
//
//    if (statusValue == StatusState.NONE && genderValue == GenderState.NONE && characterName.value == "") {
//        _state.value = _state.value.copy(
//            isFilter = false
//        )
//    } else {
//        _state.value = _state.value.copy(
//            isFilter = true
//        )
//    }
//
//    return _state.value.isFilter
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
