package com.hoomanholding.applibrary.model.data.response.order

/**
 * Created by m-latifi on 6/21/2023.
 */

data class CustomerOrderDetailModel(
    val orderId: Long,
    val billingDate: String?,
    val billingNumber: String?,
    val rowNumber: Int,
    val productCode: String?,
    val productName: String?,
    val price: Long,
    val count: Int,
    val productAmount: Long,
    val productThumbnailImageName: String
)
