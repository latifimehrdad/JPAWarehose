package com.hoomanholding.applibrary.model.data.response.report


/**
 * create by m-latifi on 4/25/2023
 */

data class VisitorSalesReportModel(
    val visitorId: Int,
    val visitorName: String?,
    val salesAmount: Long,
    val expectedSales: Long,
    val customerCount: Int,
    val availableCustomerCount: Int,
    val coverageRate: Int
)