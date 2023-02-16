package com.hoomanholding.jpawarehose.model.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.jpawarehose.model.data.database.entity.BrandEntity

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface BrandDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBrands(brands : List<BrandEntity>)

    @Query("SELECT * FROM Brand")
    fun getBrands(): List<BrandEntity>

    @Query("DELETE FROM Brand")
    fun deleteAll()

}