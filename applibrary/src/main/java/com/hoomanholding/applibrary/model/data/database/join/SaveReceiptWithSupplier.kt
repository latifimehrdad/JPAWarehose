package com.hoomanholding.applibrary.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.applibrary.model.data.database.entity.receipt.save.SaveReceiptEntity
import com.hoomanholding.applibrary.model.data.database.entity.SupplierEntity

data class SaveReceiptWithSupplier(
    @Embedded val saveReceiptEntity: SaveReceiptEntity,
    @Relation(
        entity = SupplierEntity::class,
        parentColumn = "supplierId",
        entityColumn = "id"
    )
    val supplierEntity: SupplierEntity
)
