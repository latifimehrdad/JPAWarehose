package com.hoomanholding.applibrary.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.applibrary.model.data.database.entity.ProductsEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.save.SaveReceiptAmountEntity

/**
 * Create by Mehrdad on 1/27/2023
 */
data class ReceiptAmountWhitProductModel(
    @Embedded val saveReceiptAmountEntity: SaveReceiptAmountEntity,
    @Relation(
        entity = ProductsEntity::class,
        parentColumn = "productId",
        entityColumn = "id"
    )
    val products: ProductWithBrandModel

)