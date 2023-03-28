package com.hoomanholding.applibrary.model.data.response.receipt

import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel


data class ConfirmWarehouseReceiptResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : Boolean
) : BaseResponseAbstractModel()
