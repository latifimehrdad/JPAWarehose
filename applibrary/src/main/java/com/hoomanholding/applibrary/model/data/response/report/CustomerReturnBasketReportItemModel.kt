package com.hoomanholding.applibrary.model.data.response.report

data class CustomerReturnBasketReportItemModel(
    val id: Long,
    val returnBasketId: String,
    val productCode: String,
    val productName: String,
    val count: Int
)
