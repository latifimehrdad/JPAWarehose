package com.hoomanholding.jpawarehose.model.data.database.entity.receipt.arrange

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Mehrdad on 1/22/2023
 */
@Entity(tableName = "ReceiptDetail")
data class ReceiptDetailEntity(
    val id : Long,
    val productId : Long,
    val ccBrand : Long,
    val tedad : Int
) {
    @PrimaryKey(autoGenerate = false)
    var rowId : Long = 0
}
