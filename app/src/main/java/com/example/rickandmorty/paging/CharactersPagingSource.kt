package com.example.rickandmorty.paging

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.data.dto.ApiResponse
import com.example.rickandmorty.domain.api.CharacterInteractor
import com.example.rickandmorty.presentation.characters.utils.Gender
import com.example.rickandmorty.presentation.characters.utils.Status

class CharactersPagingSource(
    private val interactor: CharacterInteractor,
    private val gender: LiveData<Gender>,
    private val status: LiveData<Status>
) : PagingSource<Int, ApiResponse.CharacterDto>() {

    override fun getRefreshKey(state: PagingState<Int, ApiResponse.CharacterDto>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiResponse.CharacterDto> {
        return try {
            val currentPage = params.key ?: 1
            val currentGender = gender.value ?: Gender.NONE
            val currentStatus = status.value ?: Status.NONE

            val response = interactor.getCharacters(currentPage, currentGender, currentStatus) //
            val data = response.body()!!.results
            val responseData = mutableListOf<ApiResponse.CharacterDto>()
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
