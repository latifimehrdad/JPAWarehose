package com.hoomanholding.applibrary.model.data.response.order

import com.hoomanholding.applibrary.model.data.enums.EnumOrderState


/**
 * create by m-latifi on 5/8/2023
 */

data class CustomerOrderModel(
    val orderId: Long,
    val orderNumber: Int,
    val orderDate: String?,
    val orderStates: EnumOrderState,
    val orderAmount: Long,
    val discount: Long,
    val finalAmount: Long
)
