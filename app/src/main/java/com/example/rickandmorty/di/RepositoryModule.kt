package com.example.rickandmorty.di

import com.example.rickandmorty.data.characters.impl.CharacterRepositoryImpl
import com.example.rickandmorty.domain.characters.CharacterRepository
import org.koin.dsl.module


val repositoryModule = module {

  //  factory { CharacterConverter() }

    single<CharacterRepository> {
        CharacterRepositoryImpl(get(), get())
    }




}
