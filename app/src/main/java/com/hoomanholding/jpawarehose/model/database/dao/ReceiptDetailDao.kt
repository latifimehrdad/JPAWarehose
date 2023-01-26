package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptDetailEntity

/**
 * Create by Mehrdad on 1/22/2023
 */

@Dao
interface ReceiptDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReceiptDetail(receiptDetails : List<ReceiptDetailEntity>)


    @Query("Select * From ReceiptDetail")
    fun getReceiptDetail() : List<ReceiptDetailEntity>


    @Query("DELETE FROM ReceiptDetail")
    fun deleteAllReceiptDetail()
}