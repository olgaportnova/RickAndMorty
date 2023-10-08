package com.example.rickandmorty.data.locations.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.locations.db.entity.LocationEntity

@Dao
interface LocationsDao {

    @Query(
        """
    SELECT * FROM locations
    WHERE (:name IS NULL OR name LIKE '%' || :name || '%')
    AND (:type IS NULL OR type LIKE '%' || :type || '%')
    AND (:dimension IS NULL OR dimension LIKE '%' || :dimension || '%')
 

"""
    )
    fun getPagingSourceLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): PagingSource<Int, LocationEntity>



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(episodes: List<LocationEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveById(episode: LocationEntity)



    @Query("SELECT * FROM locations WHERE id = :locationsId")
    fun getLocationsById(locationsId: Int): LocationEntity


}

