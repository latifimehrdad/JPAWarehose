package com.hoomanholding.applibrary.model.data.response.order

/**
 * create by m-latifi on 3/18/2023
 */

data class OrderModel(
    val id: Int,
    val orderDate: String?,
    val orderNumber: Int,
    val customerId: Int,
    val customerCode: Int,
    val customerName: String?,
    val visitorId: Int?,
    val visitorName: String?,
    val paymentTypeId: Int,
    val paymentTypeName: String?,
    val paymentDate: Short,
    val orderAmount: Long,
    val discount: Long,
    val orderFinaleAmount: Long,
    val visitorDescription: String?,
    val saleOrderState: Int,
    val saleOrderStateText: String?,
    val saleOrderDetails: List<DetailOrderModel>?,
    val storeName: String?,
    val routeName: String?,
    var select : Boolean = false
)
