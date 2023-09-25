//package com.example.rickandmorty.domain.api.impl
//
//import com.example.rickandmorty.domain.api.CharacterInteractor
//import com.example.rickandmorty.domain.api.CharacterRepository
//import com.example.rickandmorty.domain.model.Characters
//import com.example.rickandmorty.utils.Resource
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//
//
//
//class CharacterInteractorImpl(
//    private val characterRepository: CharacterRepository
//): CharacterInteractor {
//    override suspend fun getCharacters(page: Int): Flow<Pair<List<Characters>?, String?>> {
//        return flow {
//            val result = characterRepository.getCharacters(page)
//            emit(
//                when (result) {
//                    is Resource.Success -> Pair(result.data, null)
//                    is Resource.Error -> Pair(null, result.message)
//                    else -> Pair(null, "Unknown error")
//                }
//            )
//        }
//    }
//}
