package com.example.rickandmorty.presentation.episodes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickandmorty.data.characters.utils.EpisodesConverter
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.EpisodeInteractor
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.episodes.utils.EpisodeState
import com.example.rickandmorty.presentation.episodes.utils.SearchCategoriesEpisodes
import com.example.rickandmorty.presentation.episodes.utils.SearchRequestParamsEpisode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EpisodeViewModel (
    private val episodeConverter: EpisodesConverter,
    private val characterInteractor: CharacterInteractor,
    private val episodeInteractor: EpisodeInteractor
): ViewModel() {

    private val _state = MutableStateFlow(EpisodeState())
    val state: StateFlow<EpisodeState> get() = _state

    private val _nameForSearch = MutableStateFlow<String?>("")
    private val nameForSearch: StateFlow<String?> = _nameForSearch

    private val _episodeForSearch = MutableStateFlow<String?>("")
    private val episodeForSearch: StateFlow<String?> = _episodeForSearch


    private val _episodeSearchResult = MutableStateFlow<List<Episodes>>(emptyList())
    val episodeSearchResult: StateFlow<List<Episodes>> get() = _episodeSearchResult




    init {
        viewModelScope.launch {
            getListData().collect { it ->
                _state.value = _state.value.copy(
                    episodes = it
                )
            }
        }
    }



    fun getListData(): Flow<PagingData<Episodes>> {
        return combine(nameForSearch, episodeForSearch) {name, episode  ->
            SearchRequestParamsEpisode(name = name, episode=episode)
        }.flatMapLatest { params ->
            episodeInteractor.getEpisodes(params.name, params.episode)
                .flow
                .map { pagingData -> pagingData.map { episodeConverter.map(it) } }
                .cachedIn(viewModelScope)
        }
    }

    fun updateCharactersListWithSearch(selectedCategory: SearchCategoriesEpisodes, searchText: String) {
        when(selectedCategory) {
            SearchCategoriesEpisodes.NAME->_nameForSearch.value = searchText
            SearchCategoriesEpisodes.EPISODE->_episodeForSearch.value = searchText
            else -> {}
        }
    }
    fun clearTextSearchField() {
        _nameForSearch.value = ""
        _episodeForSearch.value = ""
    }

    suspend fun getEpisode(id: Int) : Episodes? {
        return withContext(Dispatchers.IO) {
            episodeInteractor.getEpisodeById(id)
        }
    }



    suspend fun getMultipleEpisodes(listOfId: List<Int>) {
        withContext(Dispatchers.IO) {
            val result = episodeInteractor.getMultipleEpisodes(listOfId)

            if (result != null) {
                _episodeSearchResult.value= result
            } else {
                _episodeSearchResult.value= emptyList()
            }
        }
    }

}
