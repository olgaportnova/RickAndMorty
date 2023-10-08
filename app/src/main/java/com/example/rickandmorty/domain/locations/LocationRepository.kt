package com.example.rickandmorty.domain.locations

import androidx.paging.Pager
import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity
import com.example.rickandmorty.data.locations.db.entity.LocationEntity
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.domain.locations.model.Locations

interface LocationRepository {
    fun getLocations(
        name: String?,
        episode: String?,
        dimension: String?
    ): Pager<Int, LocationEntity>


    suspend fun getLocationByIdFromApi(id: Int): Locations?



}