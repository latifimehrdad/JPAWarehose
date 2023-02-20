package com.hoomanholding.jpawarehose.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.jpawarehose.model.data.database.entity.BrandEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.ProductsEntity

/**
 * Created by zar on 2/20/2023.
 */

data class ProductWithBrandModel(
    @Embedded val productsEntity: ProductsEntity,
    @Relation(
        parentColumn = "brandId",
        entityColumn = "id"
    )
    val brandEntity: BrandEntity
)
