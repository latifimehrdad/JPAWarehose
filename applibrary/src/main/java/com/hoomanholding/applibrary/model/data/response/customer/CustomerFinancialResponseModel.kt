package com.hoomanholding.applibrary.model.data.response.customer

import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel


/**
 * create by m-latifi on 4/5/2023
 */

data class CustomerFinancialResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data: CustomerFinancialModel?
): BaseResponseAbstractModel()
