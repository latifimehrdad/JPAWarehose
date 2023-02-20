package com.hoomanholding.jpawarehose.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.jpawarehose.model.data.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.history.HistorySaveReceiptAmountEntity

/**
 * Created by zar on 2/20/2023.
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
