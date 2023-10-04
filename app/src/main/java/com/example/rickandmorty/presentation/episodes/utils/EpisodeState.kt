package com.example.rickandmorty.presentation.episodes.utils

import androidx.paging.PagingData
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status
import com.example.rickandmorty.domain.episodes.model.Episodes
import com.example.rickandmorty.presentation.characters.utils.SearchCategories

data class EpisodeState(
    val episodes: PagingData<Episodes>? = PagingData.empty(),
    val querySearch: String = "",
    val queryCategory: SearchCategoriesEpisodes = SearchCategoriesEpisodes.NAME,
    val toastMessage: String = "",

    )
