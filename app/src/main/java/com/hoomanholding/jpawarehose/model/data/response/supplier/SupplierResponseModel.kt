package com.hoomanholding.jpawarehose.model.data.response.supplier

import com.hoomanholding.jpawarehose.model.data.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.model.data.response.BaseResponseAbstractModel

/**
 * Create by Mehrdad on 1/18/2023
 */
data class SupplierResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<SupplierEntity>?

) : BaseResponseAbstractModel()
