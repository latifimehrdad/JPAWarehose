package com.hoomanholding.applibrary.model.data.response.product

import com.hoomanholding.applibrary.model.data.database.entity.ProductsEntity
import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel

/**
 * Create by Mehrdad on 1/18/2023
 */
data class ProductResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<ProductsEntity>?

) : BaseResponseAbstractModel()
