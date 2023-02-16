package com.hoomanholding.jpawarehose.model.data.response.receipt

import com.hoomanholding.jpawarehose.model.data.database.entity.ReceiptDetailEntity
import com.hoomanholding.jpawarehose.model.data.response.BaseResponseAbstractModel

data class ReceiptDetailResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<ReceiptDetailEntity>?

) : BaseResponseAbstractModel()