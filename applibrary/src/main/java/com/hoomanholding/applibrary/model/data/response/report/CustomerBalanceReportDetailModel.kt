package com.hoomanholding.applibrary.model.data.response.report


/**
 * create by m-latifi on 4/29/2023
 */

data class CustomerBalanceReportDetailModel(
    val transactionDate: String?,
    val description: String?,
    val debit: Long,
    val credit: Long,
    val balance: Long
)
