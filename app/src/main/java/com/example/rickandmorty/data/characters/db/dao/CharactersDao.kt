package com.example.rickandmorty.data.characters.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.characters.db.entity.CharactersEntity

@Dao
interface CharactersDao {

    @Query(
        """
    SELECT * FROM characters
    WHERE (:gender IS NULL OR gender = :gender)
    AND (:status IS NULL OR status = :status)
    AND (:name IS NULL OR name LIKE '%' || :name || '%')
    AND (:species IS NULL OR species LIKE '%' || :species || '%')
    AND (:type IS NULL OR type LIKE '%' || :type || '%')

"""
    )
     fun getPagingSourceCharacters(
        gender: String?,
        status: String?,
        name: String?,
        species: String?,
        type: String?
    ): PagingSource<Int, CharactersEntity>



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(characters: List<CharactersEntity>)


    @Query("SELECT * FROM characters WHERE id = :characterId")
    fun getCharacterById(characterId: Int): CharactersEntity


}

