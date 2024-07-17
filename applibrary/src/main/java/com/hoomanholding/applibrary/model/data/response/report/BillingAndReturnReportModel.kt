package com.hoomanholding.applibrary.model.data.response.report

/**
 * Created by m-latifi on 6/8/2023.
 */

data class BillingAndReturnReportModel(
    val id: Long,
    val customerCode: Long,
    val customerName: String?,
    val billingNumber: Long,
    val billingDate: String?,
    val amount: Long,
    val discount: Long,
    val finalAmount: Long,
    val items: List<BillingAndReturnDetailReportModel>?
)
