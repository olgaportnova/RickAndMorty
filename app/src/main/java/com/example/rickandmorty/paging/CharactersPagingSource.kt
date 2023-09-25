package com.example.rickandmorty.paging

import android.net.http.HttpException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.data.dto.ApiResponse
import com.example.rickandmorty.domain.api.CharacterRepository

class CharactersPagingSource(
    private val repository: CharacterRepository
) : PagingSource<Int, ApiResponse.CharacterDto>()
{

    override fun getRefreshKey(state: PagingState<Int, ApiResponse.CharacterDto>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiResponse.CharacterDto> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getCharactersList(currentPage)
            val data = response.body()!!.results
            val responseData = mutableListOf<ApiResponse.CharacterDto>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if(currentPage==1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e:Exception) {
            LoadResult.Error(e)
        } catch (httpE: retrofit2.HttpException){
            LoadResult.Error(httpE)

        }

    }


}