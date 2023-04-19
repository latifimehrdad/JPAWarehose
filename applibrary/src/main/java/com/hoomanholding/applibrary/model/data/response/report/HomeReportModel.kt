package com.hoomanholding.applibrary.model.data.response.report


/**
 * create by m-latifi on 4/19/2023
 */

data class HomeReportModel(
    val invoiceAmount: Long,
    val invoiceCount: Int,
    val returnInvoiceAmount: Long,
    val returnInvoiceCount: Int,
    val orderAmount: Long,
    val orderCount: Int,
    val bouncedCheckAmount: Long,
    val bouncedCheckCount: Int
)
