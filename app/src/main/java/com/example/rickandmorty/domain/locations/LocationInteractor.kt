package com.example.rickandmorty.domain.locations

import androidx.paging.PagingData
import com.example.rickandmorty.domain.locations.model.Locations
import kotlinx.coroutines.flow.Flow

interface LocationInteractor {

    fun getLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Flow<PagingData<Locations>>

    suspend fun getLocationById(id: Int): Locations?

    suspend fun getLocationByIdFromDb(id: Int): Locations?
}