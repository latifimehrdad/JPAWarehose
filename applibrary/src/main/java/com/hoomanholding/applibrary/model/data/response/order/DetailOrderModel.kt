package com.hoomanholding.applibrary.model.data.response.order


/**
 * create by m-latifi on 3/18/2023
 */

data class DetailOrderModel(
    val orderId: Int,
    val id: Int,
    val productCode: String?,
    val productName: String?,
    val count: Int,
    val price: Long,
    val amount: Long,
    val discount: Long,
    val finalAmount: Long
)
