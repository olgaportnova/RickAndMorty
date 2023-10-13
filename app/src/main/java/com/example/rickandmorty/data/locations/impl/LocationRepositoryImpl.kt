package com.example.rickandmorty.data.locations.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.rickandmorty.data.db.AppDatabase
import com.example.rickandmorty.data.locations.db.dao.LocationsDao
import com.example.rickandmorty.data.locations.db.entity.LocationEntity
import com.example.rickandmorty.data.locations.utils.LocationsConverter
import com.example.rickandmorty.data.network.LocationsRemoteMediator
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.example.rickandmorty.domain.locations.LocationRepository
import com.example.rickandmorty.domain.locations.model.Locations

@OptIn(ExperimentalPagingApi::class)
class LocationRepositoryImpl(
    private val dao: LocationsDao,
    private val api: RickAndMortyApi,
    private val locationConverter: LocationsConverter,
    private val appDatabase: AppDatabase
) : LocationRepository {

    override fun getLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Pager<Int, LocationEntity> {


        val mediator = LocationsRemoteMediator(
            appDatabase,
            api,
            locationConverter,
            name,
            type,
            dimension
        )
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = mediator,
            pagingSourceFactory = {
                dao.getPagingSourceLocations(
                    name,
                    type,
                    dimension
                )
            }
        )
    }

    override suspend fun getLocationByIdFromApi(id: Int): Locations? {
        val response = api.getLocation(id)

        return if (response.isSuccessful) {
            val locationBody = response.body()
            if (locationBody != null) {
                appDatabase.locationDao().saveById(locationConverter.dtoToEntity(locationBody))
                locationConverter.map(locationBody)
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun getLocationByIdFromDb(id: Int): Locations? {
        val locationEntity = appDatabase.locationDao().getLocationById(id)
        return if (locationEntity == null) {
            null
        } else {
            locationConverter.map(appDatabase.locationDao().getLocationById(id)!!)
        }
    }


}