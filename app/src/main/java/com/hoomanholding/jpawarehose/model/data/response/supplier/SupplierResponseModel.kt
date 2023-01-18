package com.hoomanholding.jpawarehose.model.data.response.supplier

import com.hoomanholding.jpawarehose.model.data.response.BaseResponseAbstractModel
import com.hoomanholding.jpawarehose.model.database.entity.SupplierEntity

/**
 * Create by Mehrdad on 1/18/2023
 */
data class SupplierResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<SupplierEntity>?

) : BaseResponseAbstractModel()
