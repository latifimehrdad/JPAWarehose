package com.hoomanholding.jpawarehose.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.jpawarehose.model.data.database.entity.SaveReceiptEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.SupplierEntity

data class SaveReceiptWithSupplier(
    @Embedded val saveReceiptEntity: SaveReceiptEntity,
    @Relation(
        entity = SupplierEntity::class,
        parentColumn = "supplierId",
        entityColumn = "id"
    )
    val supplierEntity: SupplierEntity
)
