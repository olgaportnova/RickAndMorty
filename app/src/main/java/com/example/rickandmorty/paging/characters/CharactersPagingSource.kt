package com.example.rickandmorty.paging.characters

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.data.characters.dto.ApiResponseCharacters
import com.example.rickandmorty.domain.characters.CharacterInteractor
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

class CharactersPagingSource(
    private val interactor: CharacterInteractor,
    private val gender: LiveData<Gender>,
    private val status: LiveData<Status>,
    private val name: LiveData<String?>,
    private val species: LiveData<String?>,
    private val type: LiveData<String?>,
) : PagingSource<Int, Characters>() {

    override fun getRefreshKey(state: PagingState<Int, Characters>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Characters> {
        return try {
            val currentPage = params.key ?: 1
            val currentGender = gender.value ?: Gender.NONE
            val currentStatus = status.value ?: Status.NONE
            val searchName = name.value
            val searchSpecies = species.value
            val searchType = type.value

            val data = interactor.getCharacters(currentPage, currentGender, currentStatus, searchName, searchSpecies, searchType)
            val responseData = mutableListOf<Characters>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (responseData.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
