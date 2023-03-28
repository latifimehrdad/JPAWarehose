package com.hoomanholding.applibrary.model.data.database.dao.receipt.history

import androidx.room.*
import com.hoomanholding.applibrary.model.data.database.entity.receipt.history.HistorySaveReceiptEntity
import com.hoomanholding.applibrary.model.data.database.join.HistorySaveReceiptWithSupplier

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface HistorySaveReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaveReceipts(saveReceipt: HistorySaveReceiptEntity): Long


    @Query("SELECT * FROM HistorySaveReceipt WHERE id = :id LIMIT 1")
    fun getSaveReceipt(id: Int): HistorySaveReceiptWithSupplier


    @Query("SELECT * FROM HistorySaveReceipt")
    fun getSaveReceipts(): List<HistorySaveReceiptWithSupplier>

    @Query("DELETE FROM HistorySaveReceipt")
    fun deleteAllRecord()


    @Query("UPDATE HistorySaveReceipt SET number = :number WHERE id > 0")
    fun updateReceiptNumber(number : Long)


    @Query("SELECT COUNT(id) FROM HistorySaveReceipt")
    fun getCount() : Int

}