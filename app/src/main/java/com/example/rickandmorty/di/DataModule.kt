package com.example.rickandmorty.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.rickandmorty.data.characters.utils.CharacterConverter
import com.example.rickandmorty.data.db.AppDatabase
import com.example.rickandmorty.data.network.CharacterRemoteMediator
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.google.gson.Gson
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://rickandmortyapi.com/"


@OptIn(ExperimentalPagingApi::class)
val dataModule = module {


    single<RickAndMortyApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)

    }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "rick_and_morty_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().charactersDao() }

    factory { Gson() }

    factory { CharacterConverter() }

    single {
        CharacterRemoteMediator(get(), get(), get())
    }

    single {
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharacterRemoteMediator(
                get(),
                get(),
                get()
            ),
            pagingSourceFactory = {
                get<AppDatabase>().charactersDao()
                    .getPagingSourceCharacters(null, null, null, null, null)
            }
        )
    }




}
