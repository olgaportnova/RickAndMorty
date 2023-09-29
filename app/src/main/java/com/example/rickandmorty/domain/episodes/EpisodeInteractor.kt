package com.example.rickandmorty.domain.episodes

import androidx.paging.Pager
import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity
import com.example.rickandmorty.domain.episodes.model.Episodes

interface EpisodeInteractor {

    fun getEpisodes(
        name: String?,
        episode: String?
    ): Pager<Int, EpisodeEntity>

    suspend fun getEpisodeById(id: Int): Episodes?
}
