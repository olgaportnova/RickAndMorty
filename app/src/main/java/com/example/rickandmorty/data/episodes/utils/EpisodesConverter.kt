package com.example.rickandmorty.data.characters.utils

import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity
import com.example.rickandmorty.data.episodes.dto.ApiResponseEpisodes
import com.example.rickandmorty.domain.episodes.model.Episodes

class EpisodesConverter {

    fun map(episodesDto: ApiResponseEpisodes.EpisodesDto): Episodes {
        return Episodes(
            id = episodesDto.id,
            name = episodesDto.name,
            air_date = episodesDto.airDate,
            episode = episodesDto.episode,
            characters = episodesDto.characters,
            url = episodesDto.url,
            created = episodesDto.created
        )
    }

    fun map(episodeEntity: EpisodeEntity): Episodes {
        return Episodes(
            id = episodeEntity.id,
            name = episodeEntity.name,
            air_date = episodeEntity.airDate,
            episode = episodeEntity.episode,
            characters = episodeEntity.characters,
            url = episodeEntity.url,
            created = episodeEntity.created
        )
    }

    fun dtoToEntity(episodesDto: ApiResponseEpisodes.EpisodesDto): EpisodeEntity {
        return EpisodeEntity(
            id = episodesDto.id,
            name = episodesDto.name,
            airDate = episodesDto.airDate,
            episode = episodesDto.episode,
            characters = episodesDto.characters,
            url = episodesDto.url,
            created = episodesDto.created
        )
    }

}
