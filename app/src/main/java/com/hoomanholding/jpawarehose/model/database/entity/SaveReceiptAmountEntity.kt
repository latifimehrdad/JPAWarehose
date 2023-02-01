package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Mehrdad on 1/27/2023
 */

@Entity(tableName = "SaveReceiptAmount")
data class SaveReceiptAmountEntity(
    val productId : Long,
    var cartonCount : Int = 0,
    var packetCount : Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}