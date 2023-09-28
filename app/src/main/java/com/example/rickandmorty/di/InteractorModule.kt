package com.example.rickandmorty.di

import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.impl.CharacterInteractorImpl
import com.google.gson.Gson
import org.koin.dsl.module


val interactorModule = module {

    single<CharacterInteractor> {
        CharacterInteractorImpl(get())
    }

    factory { Gson() }


}
