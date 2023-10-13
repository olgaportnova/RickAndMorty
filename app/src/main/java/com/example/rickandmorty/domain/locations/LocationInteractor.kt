package com.example.rickandmorty.domain.locations

import androidx.paging.Pager
import com.example.rickandmorty.data.locations.db.entity.LocationEntity
import com.example.rickandmorty.domain.locations.model.Locations

interface LocationInteractor {


    fun getLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Pager<Int, LocationEntity>

    suspend fun getLocationById(id: Int): Locations?

    suspend fun getLocationByIdFromDb(id: Int): Locations?
}