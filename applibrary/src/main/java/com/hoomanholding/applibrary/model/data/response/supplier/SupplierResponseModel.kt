package com.hoomanholding.applibrary.model.data.response.supplier

import com.hoomanholding.applibrary.model.data.database.entity.SupplierEntity
import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel

/**
 * Create by Mehrdad on 1/18/2023
 */
data class SupplierResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<SupplierEntity>?

) : BaseResponseAbstractModel()
