package com.example.rickandmorty.domain.episodes

import androidx.paging.PagingData
import com.example.rickandmorty.domain.episodes.model.Episodes
import kotlinx.coroutines.flow.Flow

interface EpisodeInteractor {

    fun getEpisodes(
        name: String?,
        episode: String?
    ): Flow<PagingData<Episodes>>


    suspend fun getEpisodeById(id: Int): Episodes?
    suspend fun getMultipleEpisodes(ids: List<Int>): List<Episodes>?
    suspend fun getEpisodeByIdFromDb(id: Int): Episodes?
    suspend fun getMultipleEpisodesFromDb(ids: List<Int>): List<Episodes>?
}
