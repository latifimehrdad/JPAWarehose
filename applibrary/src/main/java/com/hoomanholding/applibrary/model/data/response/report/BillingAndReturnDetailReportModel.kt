package com.hoomanholding.applibrary.model.data.response.report

/**
 * Created by m-latifi on 6/8/2023.
 */

data class BillingAndReturnDetailReportModel(
    val id: Long,
    val billingId: Long,
    val productCode: String?,
    val productName: String?,
    val count: Int,
    val price: Long,
    val productAmount: Long
)
