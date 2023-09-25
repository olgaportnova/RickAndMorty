package com.example.rickandmorty.di

import com.example.rickandmorty.data.network.CharacterRepositoryImpl
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.example.rickandmorty.domain.api.CharacterRepository
import com.example.rickandmorty.paging.CharactersPagingSource
import com.google.gson.Gson
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://rickandmortyapi.com/"



val dataModule = module {


    single<RickAndMortyApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)

    }

    factory { Gson() }

    factory { CharactersPagingSource(get()) }


}
