package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.*
import com.hoomanholding.jpawarehose.model.database.entity.SaveReceiptEntity

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface SaveReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaveReceipts(supplies: SaveReceiptEntity)


    @Query("SELECT * FROM SaveReceipt Limit 1")
    fun getSaveReceipts(): SaveReceiptEntity?

    @Query("DELETE FROM SaveReceipt")
    fun deleteAllRecord()
}