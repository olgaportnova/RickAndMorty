package com.example.rickandmorty.data.episodes.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.episodes.db.entity.EpisodeEntity

@Dao
interface EpisodesDao {

    @Query(
        """
    SELECT * FROM episodes
    WHERE (:name IS NULL OR name LIKE '%' || :name || '%')
    AND (:episode IS NULL OR episode LIKE '%' || :episode || '%')
 

"""
    )
    fun getPagingSourceEpisodes(
        name: String?,
        episode: String?
    ): PagingSource<Int, EpisodeEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(episodes: List<EpisodeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveById(episode: EpisodeEntity)


    @Query("SELECT * FROM episodes WHERE id = :episodeId")
    fun getEpisodeById(episodeId: Int): EpisodeEntity


    @Query("SELECT * FROM episodes WHERE id IN (:episodeIds)")
    fun getEpisodesByIds(episodeIds: List<Int>): List<EpisodeEntity>


}

