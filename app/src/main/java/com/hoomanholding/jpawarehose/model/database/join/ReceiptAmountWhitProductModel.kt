package com.hoomanholding.jpawarehose.model.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.jpawarehose.model.database.entity.BrandEntity
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.database.entity.SaveReceiptAmountEntity

/**
 * Create by Mehrdad on 1/27/2023
 */
data class ReceiptAmountWhitProductModel(
    @Embedded val saveReceiptAmountEntity: SaveReceiptAmountEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val productsEntity: ProductsEntity
)