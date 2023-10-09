package com.example.rickandmorty.presentation.characters.utils

import androidx.paging.PagingData
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status

data class CharacterState(
    val characters: PagingData<Characters>? = PagingData.empty(),
    val statusState: Status = Status.NONE,
    var genderState: Gender = Gender.NONE,
    val querySearch: String = "",
    val queryCategory: SearchCategoriesCharacters = SearchCategoriesCharacters.NAME,
    val isFilter: Boolean = false,
    val toastMessage: String = "",

    )
