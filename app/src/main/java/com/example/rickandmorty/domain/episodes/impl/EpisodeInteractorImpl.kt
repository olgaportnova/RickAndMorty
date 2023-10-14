package com.example.rickandmorty.domain.episodes.impl

import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmorty.data.characters.utils.EpisodesConverter
import com.example.rickandmorty.domain.episodes.EpisodeInteractor
import com.example.rickandmorty.domain.episodes.EpisodeRepository
import com.example.rickandmorty.domain.episodes.model.Episodes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EpisodeInteractorImpl(
    private val episodeRepository: EpisodeRepository,
    private val converter: EpisodesConverter
) : EpisodeInteractor {
    override fun getEpisodes(
        name: String?,
        episode: String?): Flow<PagingData<Episodes>> {
        return episodeRepository
            .getEpisodes(name, episode)
            .flow
            .map { pagingData -> pagingData.map { converter.map(it) } }
    }

    override suspend fun getEpisodeById(id: Int): Episodes? {
        return episodeRepository.getEpisodeByIdFromApi(id)
    }

    override suspend fun getMultipleEpisodes(ids: List<Int>): List<Episodes>? {
        return episodeRepository.getMultipleEpisodes(ids)
    }

    override suspend fun getEpisodeByIdFromDb(id: Int): Episodes? {
        return episodeRepository.getEpisodeByIdFromDb(id)
    }

    override suspend fun getMultipleEpisodesFromDb(ids: List<Int>): List<Episodes>? {
        return episodeRepository.getMultipleEpisodesFromDb(ids)
    }


}