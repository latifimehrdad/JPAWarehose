package com.hoomanholding.jpawarehose.model.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.jpawarehose.model.database.entity.SaveReceiptEntity
import com.hoomanholding.jpawarehose.model.database.entity.SupplierEntity

data class SaveReceiptWithSupplier(
    @Embedded val saveReceiptEntity: SaveReceiptEntity,
    @Relation(
        entity = SupplierEntity::class,
        parentColumn = "supplierId",
        entityColumn = "id"
    )
    val supplierEntity: SupplierEntity
)
