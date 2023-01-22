package com.hoomanholding.jpawarehose.model.data.response.brand

import com.hoomanholding.jpawarehose.model.data.response.BaseResponseAbstractModel
import com.hoomanholding.jpawarehose.model.database.entity.BrandEntity

/**
 * Create by Mehrdad on 1/18/2023
 */
data class BrandResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<BrandEntity>?

) : BaseResponseAbstractModel()
