package com.hoomanholding.jpawarehose.model.data.database.dao.receipt.save

import androidx.room.*
import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.save.SaveReceiptEntity
import com.hoomanholding.jpawarehose.model.data.database.join.SaveReceiptWithSupplier

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface SaveReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaveReceipts(saveReceipt: SaveReceiptEntity)


    @Query("SELECT * FROM SaveReceipt Limit 1")
    fun getSaveReceipt(): SaveReceiptWithSupplier?

    @Query("DELETE FROM SaveReceipt")
    fun deleteAllRecord()


    @Query("UPDATE SaveReceipt SET number = :number WHERE id > 0")
    fun updateReceiptNumber(number : Long)


    @Query("SELECT COUNT(id) FROM SaveReceipt")
    fun getCount() : Int

}