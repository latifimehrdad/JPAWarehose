package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hoomanholding.jpawarehose.model.database.join.ProductWithBrandModel

/**
 * Create by Mehrdad on 1/27/2023
 */
@Entity(tableName = "SaveReceiptEntity")
data class SaveReceiptEntity(
    val productWithBrandModel : ProductWithBrandModel
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}