package com.hoomanholding.applibrary.model.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.applibrary.model.data.database.entity.LocationEntity

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(locations : List<LocationEntity>)

    @Query("SELECT * FROM Location")
    fun getLocation(): List<LocationEntity>

    @Query("DELETE FROM Location")
    fun deleteAll()

}