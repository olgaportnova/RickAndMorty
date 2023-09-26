package com.example.rickandmorty.data.characters.utils

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
}
