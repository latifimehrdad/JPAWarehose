package com.hoomanholding.applibrary.model.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.applibrary.model.data.database.entity.SupplierEntity

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface SupplierDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSuppliers(supplies: List<SupplierEntity>)

    @Query("SELECT * FROM Supplier")
    fun getSuppliers(): List<SupplierEntity>

    @Query("DELETE FROM Supplier")
    fun deleteAll()

}