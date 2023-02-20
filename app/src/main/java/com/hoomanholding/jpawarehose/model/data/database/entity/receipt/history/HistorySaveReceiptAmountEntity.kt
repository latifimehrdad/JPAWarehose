package com.hoomanholding.jpawarehose.model.data.database.entity.receipt.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hoomanholding.jpawarehose.model.data.database.entity.ProductsEntity

/**
 * Create by Mehrdad on 1/27/2023
 */

@Entity(
    tableName = "HistorySaveReceiptAmount",
    foreignKeys = [
        ForeignKey(
            entity = ProductsEntity::class,
            childColumns = ["productId"],
            parentColumns = ["id"]
        ),
        ForeignKey(
            entity = HistorySaveReceiptEntity::class,
            childColumns = ["receiptId"],
            parentColumns = ["id"]
        )
    ]
)
data class HistorySaveReceiptAmountEntity(
    var cartonCount: Int = 0,
    var packetCount: Int = 0,
    @ColumnInfo(name = "productId") val productId: Long,
    @ColumnInfo(name = "receiptId") val receiptId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}