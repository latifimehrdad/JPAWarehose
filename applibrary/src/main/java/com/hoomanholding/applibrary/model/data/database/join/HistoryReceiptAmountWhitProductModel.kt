package com.hoomanholding.applibrary.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.applibrary.model.data.database.entity.ProductsEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.history.HistorySaveReceiptAmountEntity

/**
 * Create by Mehrdad on 1/27/2023
 */
data class HistoryReceiptAmountWhitProductModel(
    @Embedded val saveReceiptAmountEntity: HistorySaveReceiptAmountEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val productsEntity: ProductsEntity
)