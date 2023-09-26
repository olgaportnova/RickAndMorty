package com.example.rickandmorty.utils

import android.app.Application
import com.example.rickandmorty.di.dataModule
import com.example.rickandmorty.di.interactorModule
import com.example.rickandmorty.di.repositoryModule
import com.example.rickandmorty.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class App() : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }


    }

}






