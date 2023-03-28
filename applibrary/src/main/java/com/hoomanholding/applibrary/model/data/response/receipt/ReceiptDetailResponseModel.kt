package com.hoomanholding.applibrary.model.data.response.receipt

import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptDetailEntity
import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel

data class ReceiptDetailResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<ReceiptDetailEntity>?

) : BaseResponseAbstractModel()