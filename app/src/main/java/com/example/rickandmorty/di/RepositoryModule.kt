package com.example.rickandmorty.di



import com.example.rickandmorty.data.characters.impl.CharactersRepositoryImpl
import com.example.rickandmorty.data.episodes.impl.EpisodeRepositoryImpl
import com.example.rickandmorty.data.locations.impl.LocationRepositoryImpl
import com.example.rickandmorty.domain.characters.CharacterRepository
import com.example.rickandmorty.domain.episodes.EpisodeRepository
import com.example.rickandmorty.domain.locations.LocationRepository
import org.koin.dsl.module


val repositoryModule = module {



    single<CharacterRepository> {
       CharactersRepositoryImpl(get(), get(),get(), get())
    }

    single<EpisodeRepository> {
        EpisodeRepositoryImpl(get(), get(),get(), get())
    }

    single<LocationRepository> {
        LocationRepositoryImpl(get(), get(),get(), get())
    }



}
