package com.example.rickandmorty.di


import com.example.rickandmorty.presentation.characters.viewmodel.CharactersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {


    viewModel {
        CharactersViewModel(get(), get(), get())
    }

}
