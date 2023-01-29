package com.hoomanholding.jpawarehose.model.data.response.receipt

import com.hoomanholding.jpawarehose.model.data.response.BaseResponseAbstractModel
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptDetailEntity

data class ReceiptDetailResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<ReceiptDetailEntity>?

) : BaseResponseAbstractModel()