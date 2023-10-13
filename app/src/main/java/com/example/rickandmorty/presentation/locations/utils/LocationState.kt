package com.example.rickandmorty.presentation.locations.utils

import androidx.paging.PagingData
import com.example.rickandmorty.domain.locations.model.Locations
import com.example.rickandmorty.utils.SearchCategoriesLocations

data class LocationState(
    val locations: PagingData<Locations>? = PagingData.empty(),
    val querySearch: String = "",
    val queryCategory: SearchCategoriesLocations = SearchCategoriesLocations.NAME,
    val toastMessage: String = "",

    )
