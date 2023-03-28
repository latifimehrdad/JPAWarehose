package com.hoomanholding.jpamanager.model.data.response.order


/**
 * create by m-latifi on 3/18/2023
 */

data class OrderModel(
    val id: Long,
    val orderDate: String?,
    val orderNumber: Long,
    val customerId: Long,
    val customerCode: Long,
    val customerName: String?,
    val visitorId: Long?,
    val visitorName: String?,
    val paymentTypeId: Long,
    val paymentTypeName: String?,
    val paymentDate: Short,
    val orderAmount: Long,
    val discount: Int,
    val orderFinaleAmount: Long,
    val visitorDescription: String?
)
