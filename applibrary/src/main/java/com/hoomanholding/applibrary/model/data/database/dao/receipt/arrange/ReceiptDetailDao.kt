package com.hoomanholding.applibrary.model.data.database.dao.receipt.arrange

import androidx.room.*
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptDetailEntity
import com.hoomanholding.applibrary.model.data.database.join.ReceiptWithProduct

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

    @Transaction
    @Query("Select * From ReceiptDetail")
    fun getReceiptDetailJoin() : List<ReceiptWithProduct>



}