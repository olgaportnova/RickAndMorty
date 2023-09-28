package com.example.rickandmorty.presentation.characters.utils

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.rickandmorty.domain.characters.model.Characters
import com.example.rickandmorty.domain.characters.model.utils.Gender
import com.example.rickandmorty.domain.characters.model.utils.Status
import com.example.rickandmorty.utils.SearchCategories

data class CharacterState(
    val characters: PagingData<Characters>? = PagingData.empty(),
    val statusState: Status = Status.NONE,
    var genderState: Gender = Gender.NONE,
    val querySearch: String = "",
    val queryCategory: SearchCategories = SearchCategories.EMPTY,
    val isFilter: Boolean = false,
    val toastMessage: String = "",

    )
