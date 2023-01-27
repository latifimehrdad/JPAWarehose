package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.*
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.database.join.ProductWithBrandModel

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products : List<ProductsEntity>)

    @Update
    fun updateProduct(productsEntity: ProductsEntity)

    @Query("SELECT * FROM Product")
    fun getProducts(): List<ProductsEntity>

    @Transaction//join product table with brand table
    @Query("SELECT * FROM Product WHERE brandId != :brandId")
    fun getProductByIgnoreBrandId(brandId : Long) : List<ProductWithBrandModel>

    @Query("DELETE FROM Product")
    fun deleteAll()

}