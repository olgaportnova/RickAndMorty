package com.example.rickandmorty.di

import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.impl.CharacterInteractorImpl
import com.example.rickandmorty.domain.episodes.EpisodeInteractor
import com.example.rickandmorty.domain.episodes.impl.EpisodeInteractorImpl
import com.google.gson.Gson
import org.koin.dsl.module


val interactorModule = module {

    single<CharacterInteractor> {
        CharacterInteractorImpl(get())
    }

    single<EpisodeInteractor> {
        EpisodeInteractorImpl(get())
    }

    factory { Gson() }




}
