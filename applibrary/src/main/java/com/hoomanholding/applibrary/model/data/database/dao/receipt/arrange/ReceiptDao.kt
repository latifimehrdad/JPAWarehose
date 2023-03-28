package com.hoomanholding.applibrary.model.data.database.dao.receipt.arrange

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptEntity

/**
 * Create by Mehrdad on 1/22/2023
 */

@Dao
interface ReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReceipt(receipts : List<ReceiptEntity>)

    @Query("Select * From Receipt")
    fun getReceipts() : List<ReceiptEntity>


    @Query("DELETE FROM Receipt")
    fun deleteAllReceipt()


    @Query("SELECT * FROM Receipt WHERE id = :id")
    fun getReceipt(id : Long) : ReceiptEntity
}