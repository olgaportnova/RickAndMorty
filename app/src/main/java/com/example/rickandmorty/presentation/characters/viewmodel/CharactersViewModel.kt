package com.example.rickandmorty.presentation.characters.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmorty.R
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status
import com.example.rickandmorty.domain.episodes.EpisodeInteractor
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.domain.locations.LocationInteractor
import com.example.rickandmorty.presentation.characters.utils.CharacterState
import com.example.rickandmorty.presentation.characters.utils.SearchRequestParams
import com.example.rickandmorty.presentation.main.view.BaseFragmentDetails.Companion.LOCATION
import com.example.rickandmorty.presentation.main.viewmodel.BaseViewModel
import com.example.rickandmorty.utils.SearchCategories
import com.example.rickandmorty.utils.SearchCategoriesCharacters
import com.example.rickandmorty.utils.event.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(
    private val characterInteractor: CharacterInteractor,
    private val episodeInteractor: EpisodeInteractor,
    private val locationsInteractor: LocationInteractor
) : BaseViewModel(characterInteractor) {

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

    private val _character = MutableLiveData<Characters?>(null)
    val character: LiveData<Characters?> get() = _character

    private val _episodeIds = MutableLiveData<List<Int>>(emptyList())
    val episodeIds: LiveData<List<Int>> get() = _episodeIds

    private val _episodeSearchResult = MutableStateFlow<List<Episodes>>(emptyList())
    val episodeSearchResult: StateFlow<List<Episodes>> get() = _episodeSearchResult

    private val _navigateToDetails = MutableLiveData<Event<Int>>()
    val navigateToDetails: LiveData<Event<Int>> get() = _navigateToDetails


    private val _showToast = MutableLiveData<Event<Int>>()
    val showToast: LiveData<Event<Int>> get() = _showToast

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
        return combine(
            genderStateFlow,
            statusStateFlow,
            nameForSearch,
            speciesForSearch,
            typeForSearch
        ) { gender, status, name, species, type ->
            SearchRequestParams(
                gender = gender,
                status = status,
                name = name,
                species = species,
                type = type
            )
        }.flatMapLatest { params ->
            characterInteractor.getCharacters(
                params.gender,
                params.status,
                params.name,
                params.species,
                params.type
            )
        }.cachedIn(viewModelScope)
    }

    private suspend fun getCharacter(id: Int): Characters? {
        return withContext(Dispatchers.IO) {
            characterInteractor.getCharacterByIdFromApi(id)
        }
    }

    // requests to local DB
    private suspend fun getCharacterFromDb(id: Int): Characters? {
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

    fun clearFiltered() {
        _state.value = _state.value.copy(isFilter = false)
    }

    fun updateListWithSearch(selectedCategory: SearchCategories, searchText: String) {
        when (selectedCategory) {
            SearchCategoriesCharacters.NAME -> _nameForSearch.value = searchText
            SearchCategoriesCharacters.SPECIES -> _speciesForSearch.value = searchText
            SearchCategoriesCharacters.TYPE -> _typeForSearch.value = searchText
            else -> {}
        }
    }

    fun clearTextSearchField() {
        _nameForSearch.value = ""
        _speciesForSearch.value = ""
        _typeForSearch.value = ""
    }

    fun clearFiltersSearch() {
        _genderStateFlow.value = Gender.NONE
        _statusStateFlow.value = Status.NONE
    }

    fun loadCharacter(characterId: Int?) {
        viewModelScope.launch {
            _character.value = if (_isNetworkAvailable.value == true) {
                getCharacter(characterId!!)
            } else {
                getCharacterFromDb(characterId!!)
            }

            _character.value?.let {
                extractEpisodeIdsFromCharacter(it)
                if (episodeIds.value.isNullOrEmpty()) {
                    _episodeSearchResult.value = emptyList()
                } else {
                    loadEpisodes(episodeIds.value!!)
                }
            }
        }
    }

    private fun extractEpisodeIdsFromCharacter(character: Characters) {
        _episodeIds.value = character.episode.mapNotNull { url ->
            url.split("/").lastOrNull()?.toIntOrNull()
        }
    }

    private fun loadEpisodes(episodeIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isNetworkAvailable.value == true) {
                val result = episodeInteractor.getMultipleEpisodes(episodeIds)
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        _episodeSearchResult.value = result
                    } else {
                        _episodeSearchResult.value = emptyList()
                    }
                }
            } else {
                val result = episodeInteractor.getMultipleEpisodesFromDb(episodeIds)
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        _episodeSearchResult.value = result
                    } else {
                        _episodeSearchResult.value = emptyList()
                    }
                }
            }
        }
    }

    // navigation part to locations from characters details
    suspend fun navigateToDetails(type: Int) {
        val url = getUrlBasedOnType(type)
        val id = extractIdFromUrl(url)
        if (isNetworkAvailable.value == true) {
            handleNavigation(id)
        } else {
            handleOfflineNavigation(id)
        }
    }

    private fun getUrlBasedOnType(type: Int): String? {
        return if (type == LOCATION) {
            character.value?.location?.url.toString()
        } else {
            character.value?.origin?.url.toString()
        }
    }

    private fun extractIdFromUrl(url: String?): Int? {
        return url?.split("/")?.lastOrNull()?.toIntOrNull()
    }

    private fun handleNavigation(id: Int?) {
        if (id != null && id != 0) {
            _navigateToDetails.value = Event(id)
        } else {
            _showToast.value = Event(R.string.no_location)
        }
    }

    private suspend fun handleOfflineNavigation(id: Int?) {
        if (id != null && id != 0) {
            if (checkIfLocationInDb(id)) {
                _navigateToDetails.value = Event(id)
            } else {
                _showToast.value = Event(R.string.internet_is_needed)
            }
        } else {
            _showToast.value = Event(R.string.no_location)
        }
    }

    private suspend fun checkIfLocationInDb(locationId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            locationsInteractor.getLocationByIdFromDb(locationId) != null
        }
    }
}





