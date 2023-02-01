package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.database.join.ProductAmountModel
import com.hoomanholding.jpawarehose.model.database.join.ProductAmountSearchModel

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

    @RawQuery
    fun search(query: SupportSQLiteQuery): List<ProductAmountSearchModel>

    @Query("DELETE FROM Product")
    fun deleteAll()

}