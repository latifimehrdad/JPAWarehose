package com.hoomanholding.applibrary.model.data.database.entity.receipt.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hoomanholding.applibrary.model.data.database.entity.SupplierEntity

/**
 * Create by Mehrdad on 1/27/2023
 */
@Entity(tableName = "HistorySaveReceipt",
foreignKeys = [ForeignKey(
    entity = SupplierEntity::class,
    childColumns = ["supplierId"],
    parentColumns = ["id"]
)])
data class HistorySaveReceiptEntity(
    val date : String,
    val number : Long?,
    val description: String?,
    @ColumnInfo(name = "supplierId") val supplierId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}