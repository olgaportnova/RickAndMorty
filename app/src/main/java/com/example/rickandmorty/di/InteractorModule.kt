package com.example.rickandmorty.di

import com.google.gson.Gson
import org.koin.dsl.module


val interactorModule = module {

//    single<CharacterInteractor> {
//        CharacterInteractorImpl(get())
//    }

    factory { Gson() }


}
