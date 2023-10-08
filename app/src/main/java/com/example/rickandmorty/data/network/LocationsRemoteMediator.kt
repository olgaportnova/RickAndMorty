package com.example.rickandmorty.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmorty.data.db.AppDatabase
import com.example.rickandmorty.data.locations.db.entity.LocationEntity
import com.example.rickandmorty.data.locations.utils.LocationsConverter
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class LocationsRemoteMediator(
    private val appDatabase: AppDatabase,
    private val api: RickAndMortyApi,
    private val locationConverter: LocationsConverter,
    private val name: String?,
    private val type: String?,
    private val dimension: String?,

    ) : RemoteMediator<Int, LocationEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationEntity>,
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }

            }
            val response = api.getLocations(
                name = name,
                type = type,
                dimension = dimension
            )
            val data = response.body()?.results
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    //  loadKey = loadKey+
                }
                val locationEntity = data?.map { locationConverter.dtoToEntity(it) }
                if (locationEntity?.isNotEmpty() == true) {
                    appDatabase.locationDao().save(locationEntity)
                }
            }
            MediatorResult.Success(
                endOfPaginationReached = data?.isEmpty() == true
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }

    }
}