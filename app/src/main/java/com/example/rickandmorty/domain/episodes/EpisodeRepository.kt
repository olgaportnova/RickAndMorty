package com.example.rickandmorty.domain.episodes

import androidx.paging.Pager
import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity
import com.example.rickandmorty.domain.episodes.model.Episodes

interface EpisodeRepository {
    fun getEpisodes(
        name: String?,
        episode: String?
    ): Pager<Int, EpisodeEntity>


    suspend fun getEpisodeByIdFromApi(id: Int): Episodes?

    suspend fun getMultipleEpisodes(ids: List<Int>): List<Episodes>?

}