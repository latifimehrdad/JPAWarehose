package com.hoomanholding.jpawarehose.model.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.jpawarehose.model.database.entity.BrandEntity
import com.hoomanholding.jpawarehose.model.database.entity.LocationEntity
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptDetailEntity

data class ReceiptWithProduct(
    @Embedded val receiptDetailEntity: ReceiptDetailEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val productsEntity: ProductsEntity,
    @Relation(
        parentColumn = "ccBrand",
        entityColumn = "id"
    )
    val brandEntity: BrandEntity,
    @Relation(
        entity = LocationEntity::class,
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val location : List<LocationWithAmount>
)
