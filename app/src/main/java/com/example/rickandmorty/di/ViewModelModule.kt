package com.example.rickandmorty.di

import com.example.rickandmorty.presentation.characters.viewmodel.CharacterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {


    viewModel {
        CharacterViewModel(get())
    }

}
