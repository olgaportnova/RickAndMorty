package com.example.rickandmorty.di



import com.example.rickandmorty.data.characters.impl.CharactersRepositoryImpl
import com.example.rickandmorty.domain.characters.CharacterRepository
import org.koin.dsl.module


val repositoryModule = module {



    single<CharacterRepository> {
       CharactersRepositoryImpl(get(), get(),get(), get())
    }


}
