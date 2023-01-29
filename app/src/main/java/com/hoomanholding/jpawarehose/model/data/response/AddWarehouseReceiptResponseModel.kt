package com.hoomanholding.jpawarehose.model.data.response

data class AddWarehouseReceiptResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : Long
) : BaseResponseAbstractModel()
