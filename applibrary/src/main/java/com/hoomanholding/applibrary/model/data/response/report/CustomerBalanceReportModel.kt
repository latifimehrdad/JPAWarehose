package com.hoomanholding.applibrary.model.data.response.report


/**
 * create by m-latifi on 4/29/2023
 */

data class CustomerBalanceReportModel(
    val customerId: Int,
    val customerCode: String?,
    val customerName: String?,
    val storeName: String?,
    val visitorName: String?,
    val debit: Long,
    val credit: Long,
    val balance: Long
)
