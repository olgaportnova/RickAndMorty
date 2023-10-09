package com.example.rickandmorty.presentation.episodes.utils

import androidx.paging.PagingData
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.utils.SearchCategoriesEpisodes

data class EpisodeState(
    val episodes: PagingData<Episodes>? = PagingData.empty(),
    val querySearch: String = "",
    val queryCategory: SearchCategoriesEpisodes = SearchCategoriesEpisodes.NAME,
    val toastMessage: String = "",

    )
