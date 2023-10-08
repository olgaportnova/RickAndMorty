package com.example.rickandmorty.data.locations.utils

import com.example.rickandmorty.data.locations.db.entity.LocationEntity
import com.example.rickandmorty.data.locations.dto.ApiResponseLocations
import com.example.rickandmorty.domain.locations.model.Locations

class LocationsConverter {

    fun map(locationsDto: ApiResponseLocations.LocationsDto): Locations {
        return Locations(
            id = locationsDto.id,
            name = locationsDto.name,
            type = locationsDto.type,
            dimension = locationsDto.dimension,
            residents = locationsDto.residents,
            url = locationsDto.url,
            created = locationsDto.created
        )
    }

    fun map(locationsEntity: LocationEntity): Locations {
        return Locations(
            id = locationsEntity.id,
            name = locationsEntity.name,
            type = locationsEntity.type,
            dimension = locationsEntity.dimension,
            residents = locationsEntity.residents,
            url = locationsEntity.url,
            created = locationsEntity.created
        )
    }

    fun dtoToEntity(locationsDto: ApiResponseLocations.LocationsDto): LocationEntity {
        return LocationEntity(
            id = locationsDto.id,
            name = locationsDto.name,
            type = locationsDto.type,
            dimension = locationsDto.dimension,
            residents = locationsDto.residents,
            url = locationsDto.url,
            created = locationsDto.created
        )
    }

}