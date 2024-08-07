package com.hoomanholding.applibrary.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.applibrary.model.data.database.entity.BrandEntity
import com.hoomanholding.applibrary.model.data.database.entity.ProductsEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.save.SaveReceiptAmountEntity

/**
 * Create by Mehrdad on 1/27/2023
 */
data class ProductAmountSearchModel(
    @Embedded val productsEntity: ProductsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId"
    )
    var saveReceiptAmountEntity: SaveReceiptAmountEntity?,
    @Relation(
        parentColumn = "brandId",
        entityColumn = "id"
    )
    var brandEntity: BrandEntity?
)