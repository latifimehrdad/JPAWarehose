package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.hoomanholding.jpawarehose.model.database.entity.ProductSaveReceiptEntity
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.database.entity.SaveReceiptEntity

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface ProductSaveReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductSaveReceipts(products: List<ProductSaveReceiptEntity>)


    @Query("SELECT * FROM ProductSaveReceipt")
    fun getProductSaveReceipts(): List<ProductSaveReceiptEntity>

    @Query("DELETE FROM ProductSaveReceipt")
    fun deleteAllRecord()

    @Update
    fun updateProduct(productSaveReceiptEntity: ProductSaveReceiptEntity)

    @RawQuery(observedEntities = [ProductSaveReceiptEntity::class])
    fun search(query: SupportSQLiteQuery): List<ProductSaveReceiptEntity>

    @Query("SELECT COUNT(id) FROM ProductSaveReceipt WHERE cartonCount > 0 OR packetCount > 0")
    fun getProductAmount() : Int

    @Query("SELECT * FROM ProductSaveReceipt WHERE cartonCount > 0 OR packetCount > 0")
    fun getProductToSend(): List<ProductSaveReceiptEntity>
}