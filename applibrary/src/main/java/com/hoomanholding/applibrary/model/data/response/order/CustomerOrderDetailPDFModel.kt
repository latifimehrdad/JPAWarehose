package com.hoomanholding.applibrary.model.data.response.order

/**
 * Created by m-latifi on 6/20/2023.
 */

data class CustomerOrderDetailPDFModel(
    val id: Long,
    val billingNumber: Long,
    val billingDate: String?,
    val visitorName: String?,
    val salerAddress: String?,
    val customerName: String?,
    val customerCode: String?,
    val storeName: String?,
    val customerMobile: String?,
    val customerPhone: String?,
    val customerAddress: String?,
    val amount: Long,
    val discount: Long,
    val finalAmount: Long,
    val items: List<CustomerOrderDetailItemPDFModel>?
)
