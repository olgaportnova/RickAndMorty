package com.example.rickandmorty.presentation.characters.utils

import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

data class SearchRequestParams(

    var gender: Gender = Gender.NONE,
    var status : Status = Status.NONE,
    val name: String? = null,
    val species: String? = null,
    val type: String? = null
)
