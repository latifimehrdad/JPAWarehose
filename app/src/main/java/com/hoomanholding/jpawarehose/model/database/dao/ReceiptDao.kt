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

    @Query("Select * From Receipt")
    fun getReceipts() : List<ReceiptEntity>


    @Query("DELETE FROM Receipt")
    fun deleteAllReceipt()


    @Query("SELECT * FROM Receipt WHERE id = :id")
    fun getReceipt(id : Long) : ReceiptEntity
}