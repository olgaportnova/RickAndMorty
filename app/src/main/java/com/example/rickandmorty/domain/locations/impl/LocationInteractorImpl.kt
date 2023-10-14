package com.example.rickandmorty.domain.locations.impl

import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmorty.data.locations.utils.LocationsConverter
import com.example.rickandmorty.domain.locations.LocationInteractor
import com.example.rickandmorty.domain.locations.LocationRepository
import com.example.rickandmorty.domain.locations.model.Locations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationInteractorImpl(
    private val locationRepository: LocationRepository,
    private val converter: LocationsConverter
) : LocationInteractor {

    override fun getLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Flow<PagingData<Locations>> {
        return locationRepository
            .getLocations(name, type, dimension)
            .flow
            .map { pagingData -> pagingData.map { converter.map(it) } }
    }

    override suspend fun getLocationById(id: Int): Locations? {
        return locationRepository.getLocationByIdFromApi(id)
    }

    override suspend fun getLocationByIdFromDb(id: Int): Locations? {
        return locationRepository.getLocationByIdFromDb(id)
    }

}