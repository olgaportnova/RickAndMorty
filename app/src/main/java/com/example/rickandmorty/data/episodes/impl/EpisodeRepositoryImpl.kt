package com.example.rickandmorty.data.episodes.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.rickandmorty.data.characters.utils.EpisodesConverter
import com.example.rickandmorty.data.db.AppDatabase
import com.example.rickandmorty.data.episodes.db.dao.EpisodesDao
import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity
import com.example.rickandmorty.data.network.EpisodeRemoteMediator
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.example.rickandmorty.domain.episodes.EpisodeRepository
import com.example.rickandmorty.domain.episodes.model.Episodes


@OptIn(ExperimentalPagingApi::class)
class EpisodeRepositoryImpl(
    private val dao: EpisodesDao,
    private val api: RickAndMortyApi,
    private val episodeConverter: EpisodesConverter,
    private val appDatabase: AppDatabase
) : EpisodeRepository {
    override fun getEpisodes(
        name: String?,
        episode: String?
    ): Pager<Int, EpisodeEntity> {


        val mediator = EpisodeRemoteMediator(
            appDatabase,
            api,
            episodeConverter,
            name,
            episode
        )
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = mediator,
            pagingSourceFactory = {
                dao.getPagingSourceEpisodes(
                    name,
                    episode
                )
            }
        )
    }

    override suspend fun getEpisodeByIdFromApi(id: Int): Episodes? {
        val response = api.getEpisode(id)

        return if (response.isSuccessful) {
            val episodeBody = response.body()
            if (episodeBody != null) {
                appDatabase.episodeDao().saveById(episodeConverter.dtoToEntity(episodeBody))
                episodeConverter.map(episodeBody)
            } else {
                null
            }
        } else {
            null
        }
    }


    override suspend fun getMultipleEpisodes(ids: List<Int>): List<Episodes>? {
        val idsString = ids.joinToString(",")
        val response = api.getMultipleEpisodes(idsString)

        return if (response.isSuccessful) {
            val episodeBody = response.body()
            episodeBody?.let { dtoList ->
                val episodesList: MutableList<Episodes> = mutableListOf()

                dtoList.forEach { dto ->
                    val episodeEntity = episodeConverter.dtoToEntity(dto)
                    appDatabase.episodeDao().saveById(episodeEntity)
                    val episode = episodeConverter.map(dto)
                    episodesList.add(episode)
                }

                episodesList
            } ?: emptyList()
        } else {
            null
        }
    }


    }



