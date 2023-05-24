package com.hoomanholding.applibrary.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.applibrary.model.data.database.entity.BrandEntity
import com.hoomanholding.applibrary.model.data.database.entity.ProductsEntity

/**
 * Created by m-latifi on 2/20/2023.
 */

data class ProductWithBrandModel(
    @Embedded val productsEntity: ProductsEntity,
    @Relation(
        parentColumn = "brandId",
        entityColumn = "id"
    )
    val brandEntity: BrandEntity
)
