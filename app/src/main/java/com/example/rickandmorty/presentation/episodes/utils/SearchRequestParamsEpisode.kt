package com.example.rickandmorty.presentation.episodes.utils

import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

data class SearchRequestParamsEpisode(

    val name: String? = null,
    val episode: String? = null,

)
