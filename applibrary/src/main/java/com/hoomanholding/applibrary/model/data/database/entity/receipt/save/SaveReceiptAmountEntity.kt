package com.hoomanholding.applibrary.model.data.database.entity.receipt.save

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Mehrdad on 1/27/2023
 */

@Entity(tableName = "SaveReceiptAmount")
data class SaveReceiptAmountEntity(
    @PrimaryKey
    val productId : Long,
    var cartonCount : Int = 0,
    var packetCount : Int = 0
)