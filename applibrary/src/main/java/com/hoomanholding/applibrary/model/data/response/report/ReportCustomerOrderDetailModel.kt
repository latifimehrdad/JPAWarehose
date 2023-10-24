package com.hoomanholding.applibrary.model.data.response.report

/**
 * Created by m-latifi on 10/24/2023.
 */

data class ReportCustomerOrderDetailModel(
    val id: Long,
    val orderId: Int,
    val productCode: String?,
    val productName: String?,
    val productCount: Int,
    val productPrice: Long,
    val productAmount: Long
)
