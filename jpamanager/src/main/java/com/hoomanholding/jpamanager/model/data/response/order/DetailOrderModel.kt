package com.hoomanholding.jpamanager.model.data.response.order


/**
 * create by m-latifi on 3/18/2023
 */

data class DetailOrderModel(
    val orderId: Long,
    val id: Long,
    val productCode: String?,
    val productName: String?,
    val count: Int,
    val price: Long,
    val amount: Long,
    val discount: Int,
    val finalAmount: Long
)
