package com.example.rickandmorty.presentation.episodes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickandmorty.data.characters.utils.EpisodesConverter
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.episodes.EpisodeInteractor
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.episodes.utils.EpisodeState
import com.example.rickandmorty.presentation.episodes.utils.SearchRequestParamsEpisode
import com.example.rickandmorty.presentation.main.viewmodel.BaseViewModel
import com.example.rickandmorty.utils.SearchCategories
import com.example.rickandmorty.utils.SearchCategoriesEpisodes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpisodeViewModel(
    private val episodeConverter: EpisodesConverter,
    private val episodeInteractor: EpisodeInteractor,
    private val characterInteractor: CharacterInteractor
) : BaseViewModel(characterInteractor) {

    private val _state = MutableStateFlow(EpisodeState())
    val state: StateFlow<EpisodeState> get() = _state

    private val _nameForSearch = MutableStateFlow<String?>("")
    private val nameForSearch: StateFlow<String?> = _nameForSearch

    private val _episodeForSearch = MutableStateFlow<String?>("")
    private val episodeForSearch: StateFlow<String?> = _episodeForSearch

    private val _episode = MutableLiveData<Episodes?>(null)
    val episode: LiveData<Episodes?> get() = _episode

    init {
        viewModelScope.launch {
            getListData().collect { it ->
                _state.value = _state.value.copy(
                    episodes = it
                )
            }
        }
    }
    // requests to API
    private suspend fun getEpisode(id: Int): Episodes? {
        return withContext(Dispatchers.IO) {
            episodeInteractor.getEpisodeById(id)
        }
    }
    fun getListData(): Flow<PagingData<Episodes>> {
        return combine(nameForSearch, episodeForSearch) { name, episode ->
            SearchRequestParamsEpisode(name = name, episode = episode)
        }.flatMapLatest { params ->
            episodeInteractor.getEpisodes(params.name, params.episode)
                .flow
                .map { pagingData -> pagingData.map { episodeConverter.map(it) } }
                .cachedIn(viewModelScope)
        }
    }
    // requests to local DB
    private suspend fun getEpisodeFromDb(id: Int): Episodes? {
        return withContext(Dispatchers.IO) {
            episodeInteractor.getEpisodeByIdFromDb(id)
        }
    }
    // search support functions
    fun updateListWithSearch(selectedCategory: SearchCategories, searchText: String) {
        when (selectedCategory) {
            SearchCategoriesEpisodes.NAME -> _nameForSearch.value = searchText
            SearchCategoriesEpisodes.EPISODE -> _episodeForSearch.value = searchText
            else -> {}
        }
    }
    fun clearTextSearchField() {
        _nameForSearch.value = ""
        _episodeForSearch.value = ""
    }
    fun loadEpisode(episodeId: Int?) {
        viewModelScope.launch {
            _episode.value = if (_isNetworkAvailable.value == true) {
                getEpisode(episodeId!!)
            } else {
                getEpisodeFromDb(episodeId!!)
            }
            _episode.value?.let {
                extractCharactersIdsFromEpisode(it)
                if (charactersIds.value.isNullOrEmpty()) {
                    _charactersSearchResult.value = emptyList()
                } else {
                    loadCharacters(charactersIds.value!!)
                }
            }
        }
    }
    private fun extractCharactersIdsFromEpisode(episode: Episodes) {
        _charactersIds.value = episode.characters.mapNotNull { url ->
            url.split("/").lastOrNull()?.toIntOrNull()
        }
    }

}
