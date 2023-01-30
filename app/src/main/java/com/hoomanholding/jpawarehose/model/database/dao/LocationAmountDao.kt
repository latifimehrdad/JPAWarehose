package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.jpawarehose.model.database.entity.LocationAmountEntity
import com.hoomanholding.jpawarehose.model.database.entity.LocationEntity

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface LocationAmountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocationAmount(locationAmount : LocationAmountEntity)

    @Query("SELECT * FROM LocationAmount")
    fun getLocation(): List<LocationAmountEntity>

    @Query("DELETE FROM LocationAmount")
    fun deleteAll()

    @Query("SELECT SUM(amount) FROM LocationAmount WHERE productId = :productId AND locationId <> :locationId")
    fun getSumAmount(locationId : Long, productId : Long) : Long

    @Query("SELECT SUM(amount) FROM LocationAmount")
    fun getSumAmount() : Long

}