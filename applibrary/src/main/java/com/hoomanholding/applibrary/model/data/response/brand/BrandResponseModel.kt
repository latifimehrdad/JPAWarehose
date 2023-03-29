package com.hoomanholding.applibrary.model.data.response.brand

import com.hoomanholding.applibrary.model.data.database.entity.BrandEntity
import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel

/**
 * Create by Mehrdad on 1/18/2023
 */
data class BrandResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<BrandEntity>?

) : BaseResponseAbstractModel()