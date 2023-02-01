package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.hoomanholding.jpawarehose.model.database.entity.SaveReceiptAmountEntity
import com.hoomanholding.jpawarehose.model.database.join.ReceiptAmountWhitProductModel

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface SaveReceiptAmountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaveReceiptAmount(amount: SaveReceiptAmountEntity)

    @Query("SELECT * FROM SaveReceiptAmount")
    fun getProductSaveReceipts(): List<SaveReceiptAmountEntity>

    @Query("DELETE FROM SaveReceiptAmount")
    fun deleteAllRecord()

    @Query("SELECT * FROM SaveReceiptAmount WHERE cartonCount > 0 OR packetCount > 0")
    fun getReceiptAmountWithProduct() : List<ReceiptAmountWhitProductModel>

/*    @Query("SELECT COUNT(id) FROM SaveReceiptAmount WHERE cartonCount > 0 OR packetCount > 0")
    fun getProductAmount() : Int*/

/*    @Query("SELECT * FROM SaveReceiptAmount WHERE cartonCount > 0 OR packetCount > 0")
    fun getProductToSend(): List<SaveReceiptAmountEntity>*/
}