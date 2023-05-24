package com.hoomanholding.applibrary.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.applibrary.model.data.database.entity.ProductsEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.history.HistorySaveReceiptAmountEntity

/**
 * Created by m-latifi on 2/20/2023.
 */

data class ProductWithHistoryReceipt(
    @Embedded val amount: HistorySaveReceiptAmountEntity,
    @Relation(
        entity = ProductsEntity::class,
        parentColumn = "productId",
        entityColumn = "id"
    )
    val products: ProductWithBrandModel
)
