//package com.example.rickandmorty.domain.characters.impl
//
//import androidx.paging.PagingData
//import com.example.rickandmorty.domain.characters.CharacterInteractor
//import com.example.rickandmorty.domain.characters.CharacterRepository
//import com.example.rickandmorty.domain.characters.model.Characters
//import com.example.rickandmorty.domain.characters.model.utils.Gender
//import com.example.rickandmorty.domain.characters.model.utils.Status
//import kotlinx.coroutines.flow.Flow
//
//class CharacterInteractorImpl(
//    private val characterRepository: CharacterRepository
//) : CharacterInteractor {
//    override fun getCharactersPaged(
//        gender: Gender?
//    ): Flow<PagingData<Characters>> {
//        return characterRepository.getCharactersPaged(gender?.title)
//    }
//
//}
