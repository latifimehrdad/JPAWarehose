package com.hoomanholding.jpawarehose.model.data.response.receipt

import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.arrange.ReceiptEntity
import com.hoomanholding.jpawarehose.model.data.response.BaseResponseAbstractModel

/**
 * Create by Mehrdad on 1/22/2023
 */
data class ReceiptResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<ReceiptEntity>?
) : BaseResponseAbstractModel()
