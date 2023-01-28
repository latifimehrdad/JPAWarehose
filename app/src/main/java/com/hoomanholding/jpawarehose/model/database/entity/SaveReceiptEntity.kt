package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Mehrdad on 1/27/2023
 */
@Entity(tableName = "SaveReceipt")
data class SaveReceiptEntity(
    val supplierEntity: SupplierEntity,
    val date : String,
    val number : Long?
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}