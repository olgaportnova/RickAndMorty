package com.example.rickandmorty.di

import com.example.rickandmorty.domain.api.CharacterInteractor
import com.example.rickandmorty.domain.api.impl.CharacterInteractorImpl
import com.google.gson.Gson
import org.koin.dsl.module


val interactorModule = module {

    single<CharacterInteractor> {
        CharacterInteractorImpl(get())
    }

    factory { Gson() }


}
