package com.example.rickandmorty.di

import androidx.room.Room
import com.example.rickandmorty.data.characters.utils.CharacterConverter
import com.example.rickandmorty.data.characters.utils.EpisodesConverter
import com.example.rickandmorty.data.db.AppDatabase
import com.example.rickandmorty.data.db.AppDatabase.Companion.MIGRATION_2_3
import com.example.rickandmorty.data.locations.utils.LocationsConverter
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://rickandmortyapi.com/"

val dataModule = module {

    val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    single<RickAndMortyApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)

    }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "rick_and_morty_db")
            .addMigrations(MIGRATION_2_3)
            .build()
    }
    single { get<AppDatabase>().charactersDao() }
    single { get<AppDatabase>().episodeDao() }
    single { get<AppDatabase>().locationDao() }

    factory { Gson() }

    factory { CharacterConverter() }
    factory { EpisodesConverter() }
    factory { LocationsConverter() }

}
