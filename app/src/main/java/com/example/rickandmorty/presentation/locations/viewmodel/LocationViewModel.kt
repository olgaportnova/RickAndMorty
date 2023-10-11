package com.example.rickandmorty.presentation.locations.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickandmorty.data.locations.utils.LocationsConverter
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.domain.locations.LocationInteractor
import com.example.rickandmorty.domain.locations.model.Locations
import com.example.rickandmorty.presentation.locations.utils.LocationState
import com.example.rickandmorty.presentation.locations.utils.SearchRequestParamsLocations
import com.example.rickandmorty.utils.NetworkUtils
import com.example.rickandmorty.utils.SearchCategories
import com.example.rickandmorty.utils.SearchCategoriesLocations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LocationViewModel (
    private val locationConverter: LocationsConverter,
    private val locationInteractor: LocationInteractor,
    private val characterInteractor: CharacterInteractor
): ViewModel() {

    private val _state = MutableStateFlow(LocationState())
    val state: StateFlow<LocationState> get() = _state

    private val _nameForSearch = MutableStateFlow<String?>("")
    private val nameForSearch: StateFlow<String?> = _nameForSearch

    private val _typeForSearch = MutableStateFlow<String?>("")
    private val typeForSearch: StateFlow<String?> = _typeForSearch

    private val _dimensionForSearch = MutableStateFlow<String?>("")
    private val dimensionForSearch: StateFlow<String?> = _dimensionForSearch


    private val _locationSearchResult = MutableStateFlow<List<Locations>>(emptyList())
    val locationSearchResult: StateFlow<List<Locations>> get() = _locationSearchResult

    private val _isNetworkAvailable = MutableLiveData<Boolean>(false)
    val isNetworkAvailable: LiveData<Boolean> = _isNetworkAvailable

    private val _location = MutableLiveData<Locations?>(null)
    val location: LiveData<Locations?> get() = _location

    private val _charactersIds = MutableLiveData<List<Int>>(emptyList())
    val charactersIds: LiveData<List<Int>> get() = _charactersIds

    private val _charactersSearchResult = MutableStateFlow<List<Characters>>(emptyList())
    val charactersSearchResult: StateFlow<List<Characters>> get() = _charactersSearchResult




    init {
        viewModelScope.launch {
            getListData().collect { it ->
                _state.value = _state.value.copy(
                    locations = it
                )
            }
        }
    }


    // requests to API
    fun getListData(): Flow<PagingData<Locations>> {
        return combine(nameForSearch, typeForSearch, dimensionForSearch) {name, type, dimension ->
            SearchRequestParamsLocations(name = name, type=type, dimension= dimension)
        }.flatMapLatest { params ->
            locationInteractor.getLocations(params.name, params.type,params.dimension)
                .flow
                .map {  pagingData -> pagingData.map { locationConverter.map(it) }  }
                .cachedIn(viewModelScope)
        }
    }
    suspend fun getLocation(id: Int) : Locations? {
        return withContext(Dispatchers.IO) {
            locationInteractor.getLocationById(id)
        }
    }

    // requests to local DB
    suspend fun getLocationFromDb(id: Int) : Locations? {
        return withContext(Dispatchers.IO) {
            locationInteractor.getLocationByIdFromDb(id)
        }
    }

    // search support functions
    fun updateListWithSearch(selectedCategory: SearchCategories, searchText: String) {
        when(selectedCategory) {
            SearchCategoriesLocations.NAME->_nameForSearch.value = searchText
            SearchCategoriesLocations.TYPE->_typeForSearch.value = searchText
            SearchCategoriesLocations.DIMENSION->_dimensionForSearch.value = searchText
            else -> {}
        }
    }
    fun clearTextSearchField() {
        _nameForSearch.value = ""
        _typeForSearch.value = ""
        _dimensionForSearch.value = ""
    }

    fun checkNetworkAvailability(context: Context) {
        viewModelScope.launch {
            _isNetworkAvailable.value = NetworkUtils.isNetworkAvailable(context)
        }
    }

    fun loadLocation(locationId: Int?) {
        viewModelScope.launch {
            _location.value = if (_isNetworkAvailable.value == true) {
                getLocation(locationId!!)
            } else {
                getLocationFromDb(locationId!!)
            }

            _location.value?.let {
                extractCharactersIdsFromLocation(it)
                if (charactersIds.value.isNullOrEmpty()) {
                    _charactersSearchResult.value= emptyList()
                } else {
                    loadCharacters(charactersIds.value!!)
                }
            }
        }
    }

    private fun extractCharactersIdsFromLocation(location: Locations) {
        _charactersIds.value = location.residents.mapNotNull { url ->
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


