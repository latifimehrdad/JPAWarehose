package com.hoomanholding.jpawarehose.model.data.response.receipt

import com.hoomanholding.jpawarehose.model.data.response.BaseResponseAbstractModel
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptEntity

/**
 * Create by Mehrdad on 1/22/2023
 */
data class ReceiptResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<ReceiptEntity>
) : BaseResponseAbstractModel()
