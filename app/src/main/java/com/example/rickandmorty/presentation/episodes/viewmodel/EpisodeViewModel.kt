package com.example.rickandmorty.presentation.episodes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickandmorty.R
import com.example.rickandmorty.data.characters.utils.EpisodesConverter
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.EpisodeInteractor
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.episodes.utils.EpisodeState
import com.example.rickandmorty.presentation.episodes.utils.SearchRequestParamsEpisode
import com.example.rickandmorty.utils.Event
import com.example.rickandmorty.utils.NetworkUtils
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


class EpisodeViewModel (
    private val episodeConverter: EpisodesConverter,
    private val episodeInteractor: EpisodeInteractor,
    private val characterInteractor: CharacterInteractor
): ViewModel() {

    private val _state = MutableStateFlow(EpisodeState())
    val state: StateFlow<EpisodeState> get() = _state

    private val _nameForSearch = MutableStateFlow<String?>("")
    private val nameForSearch: StateFlow<String?> = _nameForSearch

    private val _episodeForSearch = MutableStateFlow<String?>("")
    private val episodeForSearch: StateFlow<String?> = _episodeForSearch


    private val _episodeSearchResult = MutableStateFlow<List<Episodes>>(emptyList())
    val episodeSearchResult: StateFlow<List<Episodes>> get() = _episodeSearchResult

    private val _isNetworkAvailable = MutableLiveData<Boolean>(false)
    val isNetworkAvailable: LiveData<Boolean> = _isNetworkAvailable

    private val _episode = MutableLiveData<Episodes?>(null)
    val episode: LiveData<Episodes?> get() = _episode

    private val _charactersIds = MutableLiveData<List<Int>>(emptyList())
    val charactersIds: LiveData<List<Int>> get() = _charactersIds

    private val _charactersSearchResult = MutableStateFlow<List<Characters>>(emptyList())
    val charactersSearchResult: StateFlow<List<Characters>> get() = _charactersSearchResult

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

    // requests to local DB
    suspend fun getMultipleEpisodesFromDb(listOfId: List<Int>) {
        withContext(Dispatchers.IO) {
            val result = episodeInteractor.getMultipleEpisodesFromDb(listOfId)
            if (result != null) {
                _episodeSearchResult.value= result
            } else {
                _episodeSearchResult.value= emptyList()
            }
        }
    }
    suspend fun getEpisodeFromDb(id: Int) : Episodes? {
        return withContext(Dispatchers.IO) {
            episodeInteractor.getEpisodeByIdFromDb(id)
        }
    }

    // search support functions
    fun updateListWithSearch(selectedCategory: SearchCategories, searchText: String) {
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

    fun checkNetworkAvailability(context: Context) {
        viewModelScope.launch {
            _isNetworkAvailable.value = NetworkUtils.isNetworkAvailable(context)
        }
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
                    _charactersSearchResult.value= emptyList()
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


    private fun loadCharacters(charactersIds: List<Int>) {
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
