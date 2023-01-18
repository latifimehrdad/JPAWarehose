package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products : List<ProductsEntity>)

    @Query("SELECT * FROM Product")
    fun getSuppliers(): List<ProductsEntity>

    @Query("DELETE FROM Product")
    fun deleteAll()

}