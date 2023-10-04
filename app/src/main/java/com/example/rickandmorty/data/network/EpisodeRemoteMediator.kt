package com.example.rickandmorty.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmorty.data.characters.utils.EpisodesConverter
import com.example.rickandmorty.data.db.AppDatabase
import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class EpisodeRemoteMediator(
    private val appDatabase: AppDatabase,
    private val api: RickAndMortyApi,
    private val episodeConverter: EpisodesConverter,
    private val name: String?,
    private val episode: String?,

) : RemoteMediator<Int, EpisodeEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeEntity>,
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
            val response = api.getEpisodes(
                name = name,
                episode = episode
            )
            val data = response.body()?.results
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    //  loadKey = loadKey+
                }
                val episodeEntity = data?.map { episodeConverter.dtoToEntity(it) }
                if (episodeEntity?.isNotEmpty() == true) {
                    appDatabase.episodeDao().save(episodeEntity)
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