package com.example.rickandmorty.di

import com.example.rickandmorty.data.network.CharacterConverter
import com.example.rickandmorty.data.network.CharacterRepositoryImpl
import com.example.rickandmorty.domain.api.CharacterRepository
import org.koin.dsl.module


val repositoryModule = module {

    single<CharacterRepository> {
        CharacterRepositoryImpl(get(), get())
    }

    factory { CharacterConverter() }


}
