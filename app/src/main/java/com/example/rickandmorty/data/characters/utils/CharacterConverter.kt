package com.example.rickandmorty.data.characters.utils

import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.data.characters.dto.ApiResponseCharacters
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.Location
import com.example.rickandmorty.domain.characters.model.Origin

class CharacterConverter {

    fun map(characterDto: ApiResponseCharacters.CharacterDto): Characters {
        return Characters(
            id = characterDto.id,
            name = characterDto.name,
            status = characterDto.status,
            species = characterDto.species,
            type = characterDto.type,
            gender = characterDto.gender,
            origin = Origin(
                name = characterDto.origin.name,
                url = characterDto.origin.url
            ),
            location = Location(
                name = characterDto.location.name,
                url = characterDto.location.url
            ),
            image = characterDto.image,
            episode = characterDto.episode,
            url = characterDto.url,
            created = characterDto.created
        )
    }

    fun map(characterEntity: CharactersEntity): Characters {
        return Characters(
            id = characterEntity.id,
            name = characterEntity.name,
            status = characterEntity.status,
            species = characterEntity.species,
            type = characterEntity.type,
            gender = characterEntity.gender,
            origin = Origin(
                name = characterEntity.origin.name,
                url = characterEntity.origin.url
            ),
            location = Location(
                name = characterEntity.location.name,
                url = characterEntity.location.url
            ),
            image = characterEntity.image,
            episode = characterEntity.episode,
            url = characterEntity.url,
            created = characterEntity.created
        )
    }

    fun dtoToEntity(characterDto: ApiResponseCharacters.CharacterDto): CharactersEntity {
        return CharactersEntity(
            id = characterDto.id,
            name = characterDto.name,
            status = characterDto.status,
            species = characterDto.species,
            type = characterDto.type,
            gender = characterDto.gender,
            origin = com.example.rickandmorty.data.characters.db.entity.Origin(
                name = characterDto.origin.name,
                url = characterDto.origin.url
            ),
            location = com.example.rickandmorty.data.characters.db.entity.Location(
                name = characterDto.location.name,
                url = characterDto.location.url
            ),
            image = characterDto.image,
            episode = characterDto.episode,
            url = characterDto.url,
            created = characterDto.created
        )
    }

}
