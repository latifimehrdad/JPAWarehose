package com.hoomanholding.jpawarehose.model.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.jpawarehose.model.database.entity.BrandEntity
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity

/**
 * Create by Mehrdad on 1/27/2023
 */
data class ProductWithBrandModel(
    @Embedded val productsEntity: ProductsEntity,
    @Relation(
        parentColumn = "brandId",
        entityColumn = "id"
    )
    var brandEntity: BrandEntity?
)