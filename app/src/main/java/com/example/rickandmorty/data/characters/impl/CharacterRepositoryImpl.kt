package com.example.rickandmorty.data.characters.impl

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.rickandmorty.data.characters.db.dao.CharactersDao
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.data.characters.utils.CharacterConverter
import com.example.rickandmorty.data.db.AppDatabase
import com.example.rickandmorty.data.network.CharacterRemoteMediator
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.example.rickandmorty.domain.characters.CharacterRepository
import com.example.rickandmorty.domain.characters.model.Characters

@OptIn(ExperimentalPagingApi::class)
class CharactersRepositoryImpl(
    private val dao: CharactersDao,
    private val api: RickAndMortyApi,
    private val characterConverter: CharacterConverter,
    private val appDatabase: AppDatabase
) : CharacterRepository {
    override fun getCharacters(
        gender: String,
        status: String,
        name: String?,
        species: String?,
        type: String?
    ): Pager<Int, CharactersEntity> {


        val mediator = CharacterRemoteMediator(
            appDatabase,
            api,
            characterConverter,
            gender,
            status,
            name,
            species,
            type
        )
        var genderForRoom: String? = gender
        var statusForRoom: String? = status
        if (gender == "") {
            genderForRoom = null
        }
        if (status == "") {
            statusForRoom = null
        }


        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = mediator,
            pagingSourceFactory = {
                dao.getPagingSourceCharacters(
                    genderForRoom,
                    statusForRoom,
                    name,
                    species,
                    type
                )
            }
        )
    }


    override suspend fun getCharacterByIdFromApi(id: Int): Characters? {
        val response = api.getCharacter(id)

        if (response.isSuccessful) {
            val characterBody = response.body()
            Log.d ("TAG123", "characterBody = $characterBody")
            if (characterBody != null) {
                appDatabase.charactersDao().saveById(characterConverter.dtoToEntity(characterBody))
                return characterConverter.map(characterBody)
            } else {
                return null
            }
        } else {
            return null
        }
    }



}
