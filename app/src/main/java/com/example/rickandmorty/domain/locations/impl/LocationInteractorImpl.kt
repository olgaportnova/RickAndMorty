package com.example.rickandmorty.domain.locations.impl

import androidx.paging.Pager
import com.example.rickandmorty.data.locations.db.entity.LocationEntity
import com.example.rickandmorty.domain.locations.LocationInteractor
import com.example.rickandmorty.domain.locations.LocationRepository
import com.example.rickandmorty.domain.locations.model.Locations

class LocationInteractorImpl(
    private val locationRepository: LocationRepository
) : LocationInteractor {

    override fun getLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Pager<Int, LocationEntity> {
        return locationRepository.getLocations(name, type, dimension)
    }

    override suspend fun getLocationById(id: Int): Locations? {
        return locationRepository.getLocationByIdFromApi(id)
    }

    override suspend fun getLocationByIdFromDb(id: Int): Locations? {
        return locationRepository.getLocationByIdFromDb(id)
    }

}