package com.hoomanholding.applibrary.model.data.response.report

import java.util.UUID

data class CustomerReturnBasketReportModel(
    val id: UUID,
    val returnBasketDate: String,
    val txtState: String,
    val returnBasketNumber: Long,
    val items: List<CustomerReturnBasketReportItemModel>?
)
