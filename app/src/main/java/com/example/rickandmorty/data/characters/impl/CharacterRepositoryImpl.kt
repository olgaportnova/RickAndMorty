//package com.example.rickandmorty.data.characters.impl
//
//import android.util.Log
//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.Pager
//import androidx.paging.map
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import com.example.rickandmorty.data.characters.db.dao.CharactersDao
//import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
//import com.example.rickandmorty.data.characters.utils.CharacterConverter
//import com.example.rickandmorty.data.db.AppDatabase
//import com.example.rickandmorty.data.network.CharacterRemoteMediator
//import com.example.rickandmorty.data.network.RickAndMortyApi
//import com.example.rickandmorty.domain.characters.CharacterRepository
//import com.example.rickandmorty.domain.characters.model.Characters
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//@OptIn(ExperimentalPagingApi::class)
//class CharactersRepositoryImpl(
//    private val dao: CharactersDao,
//    private val api: RickAndMortyApi,
//    private val characterConverter: CharacterConverter,
//    private val appDatabase: AppDatabase,
//): CharacterRepository {
//    override fun getCharactersPaged(
//        gender: String?
//    ): Flow<PagingData<Characters>> {
//
//        val mediator = CharacterRemoteMediator(appDatabase, api, characterConverter)
//        mediator.gender = gender
//
//        val pager = Pager(
//            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
//            remoteMediator = mediator,
//            pagingSourceFactory = { dao.getPagingSourceCharacters(gender,null,null,null, null) }
//        )
//        val characters =  pager.flow
//            .map { pagingData ->
//                logPagingData(pagingData)
//                pagingData.map { characterConverter.map(it) }
//            }
//        return characters
//    }
//
//    private fun logPagingData(pagingData: PagingData<CharactersEntity>) {
//        val items = pagingData
//            .map { character ->
//                "Character: ${character.name}, Status: ${character.status}"
//            }
//
//
//        Log.d("CharactersRepositoryImpl", "Paging Data:\n$items")
//    }
//}
