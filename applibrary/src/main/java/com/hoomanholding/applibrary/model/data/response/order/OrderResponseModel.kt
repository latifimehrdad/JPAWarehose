package com.hoomanholding.applibrary.model.data.response.order

import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel


/**
 * create by m-latifi on 3/18/2023
 */

data class OrderResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data: List<OrderModel>?
): BaseResponseAbstractModel()