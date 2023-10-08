package com.example.rickandmorty.di


import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import com.example.rickandmorty.presentation.episodes.viewmodel.EpisodeViewModel
import com.example.rickandmorty.presentation.locations.viewmodel.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {


    viewModel {
        CharactersViewModel(get(), get(), get())
    }

    viewModel {
        EpisodeViewModel(get(), get(), get())
    }

    viewModel {
        LocationViewModel(get(), get(), get())
    }

}
