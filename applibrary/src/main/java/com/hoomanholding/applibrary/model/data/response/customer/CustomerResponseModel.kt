package com.hoomanholding.applibrary.model.data.response.customer

import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel


/**
 * create by m-latifi on 4/5/2023
 */

data class CustomerResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data: List<CustomerModel>?
): BaseResponseAbstractModel()
