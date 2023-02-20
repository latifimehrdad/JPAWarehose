package com.hoomanholding.jpawarehose.model.data.database.dao.receipt.history

import androidx.room.*
import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.history.HistorySaveReceiptAmountEntity
import com.hoomanholding.jpawarehose.model.data.database.join.HistoryReceiptAmountWhitProductModel

/**
 * Create by Mehrdad on 1/18/2023
 */

@Dao
interface HistorySaveReceiptAmountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaveReceiptAmount(amounts: List<HistorySaveReceiptAmountEntity>)

    @Query("SELECT * FROM HistorySaveReceiptAmount")
    fun getProductSaveReceipts(): List<HistorySaveReceiptAmountEntity>

    @Query("DELETE FROM HistorySaveReceiptAmount")
    fun deleteAllRecord()

    @Query("SELECT * FROM HistorySaveReceiptAmount WHERE cartonCount > 0 OR packetCount > 0")
    fun getReceiptAmountWithProduct() : List<HistoryReceiptAmountWhitProductModel>

/*    @Query("SELECT COUNT(id) FROM SaveReceiptAmount WHERE cartonCount > 0 OR packetCount > 0")
    fun getProductAmount() : Int*/

/*    @Query("SELECT * FROM SaveReceiptAmount WHERE cartonCount > 0 OR packetCount > 0")
    fun getProductToSend(): List<SaveReceiptAmountEntity>*/
}