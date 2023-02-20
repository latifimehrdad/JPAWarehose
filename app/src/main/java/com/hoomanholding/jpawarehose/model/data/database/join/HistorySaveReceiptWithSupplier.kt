package com.hoomanholding.jpawarehose.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.jpawarehose.model.data.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.history.HistorySaveReceiptAmountEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.history.HistorySaveReceiptEntity

data class HistorySaveReceiptWithSupplier(
    @Embedded val saveReceiptEntity: HistorySaveReceiptEntity,
    @Relation(
        entity = SupplierEntity::class,
        parentColumn = "supplierId",
        entityColumn = "id"
    )
    val supplierEntity: SupplierEntity,
    @Relation(
        entity = HistorySaveReceiptAmountEntity::class,
        parentColumn = "id",
        entityColumn = "receiptId"
    )
    val amounts: List<ProductWithHistoryReceipt>,

)
