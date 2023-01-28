package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptDetailEntity
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptEntity

/**
 * Create by Mehrdad on 1/22/2023
 */

@Dao
interface ReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReceipt(receipts : List<ReceiptEntity>)


/*    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReceiptDetail(receiptDetails : List<ReceiptDetailEntity>)*/

    @Query("Select * From Receipt")
    fun getReceipt() : List<ReceiptEntity>


    @Query("Select * From ReceiptDetail")
    fun getReceiptDetail() : List<ReceiptDetailEntity>


    @Query("DELETE FROM Receipt")
    fun deleteAllReceipt()

    @Query("DELETE FROM ReceiptDetail")
    fun deleteAllReceiptDetail()
}