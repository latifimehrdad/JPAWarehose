package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hoomanholding.jpawarehose.model.data.response.receipt.WarehouseReceiptProductLocationModel

/**
 * Create by Mehrdad on 1/22/2023
 */
@Entity(tableName = "ReceiptDetail")
data class ReceiptDetailEntity(
    val codeKala : Int,
    val nameKala : String?,
    val ccBrand : Int,
    var tedad : Int
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}
