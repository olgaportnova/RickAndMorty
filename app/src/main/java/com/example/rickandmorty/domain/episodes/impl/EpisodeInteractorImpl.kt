package com.example.rickandmorty.domain.episodes.impl

import androidx.paging.Pager
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity
import com.example.rickandmorty.domain.episodes.EpisodeInteractor
import com.example.rickandmorty.domain.episodes.EpisodeRepository
import com.example.rickandmorty.domain.episodes.model.Episodes

class EpisodeInteractorImpl(
   private val episodeRepository: EpisodeRepository
) : EpisodeInteractor {
    override fun getEpisodes(name: String?, episode: String?): Pager<Int, EpisodeEntity> {
       return episodeRepository.getEpisodes(name, episode)
    }

    override suspend fun getEpisodeById(id: Int): Episodes? {
        return episodeRepository.getEpisodeByIdFromApi(id)
    }

    override suspend fun getMultipleEpisodes(ids: List<Int>): List<Episodes>?{
        return episodeRepository.getMultipleEpisodes(ids)
    }

    override suspend fun getEpisodeByIdFromDb(id: Int): Episodes? {
        return episodeRepository.getEpisodeByIdFromDb(id)
    }

    override suspend fun getMultipleEpisodesFromDb(ids: List<Int>): List<Episodes>? {
        return episodeRepository.getMultipleEpisodesFromDb(ids)
    }



}