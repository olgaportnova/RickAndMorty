package com.example.rickandmorty.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity
import com.example.rickandmorty.data.characters.utils.CharacterConverter
import com.example.rickandmorty.data.db.AppDatabase
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val appDatabase: AppDatabase,
    private val api: RickAndMortyApi,
    private val characterConverter: CharacterConverter,
    private val gender: String,
    private val status: String,
    private val name: String?,
    private val species: String?,
    private val type: String?
) : RemoteMediator<Int, CharactersEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharactersEntity>,
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }

            }
            val response = api.getCharacters(
                page = loadKey,
                gender = gender,
                status = status,
                name = name,
                species = species,
                type = type
            )
            val data = response.body()?.results
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    //  loadKey = loadKey+
                }
                val charactersEntity = data?.map { characterConverter.dtoToEntity(it) }
                if (charactersEntity?.isNotEmpty() == true) {
                    appDatabase.charactersDao().save(charactersEntity)
                }
            }
            MediatorResult.Success(
                endOfPaginationReached = data?.isEmpty() == true
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }

    }
}