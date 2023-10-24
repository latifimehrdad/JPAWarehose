package com.hoomanholding.applibrary.model.data.response.report

/**
 * Created by m-latifi on 10/24/2023.
 */

data class ReportCustomerOrderModel(
    val id: Long,
    val customerCode: String?,
    val customerName: String?,
    val storeName: String?,
    val orderNumber: Int,
    val orderDate: String?,
    val orderAmount: Long,
    val items: List<ReportCustomerOrderDetailModel>?
)
