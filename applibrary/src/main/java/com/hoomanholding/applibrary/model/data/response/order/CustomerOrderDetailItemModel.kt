package com.hoomanholding.applibrary.model.data.response.order

/**
 * Created by m-latifi on 6/20/2023.
 */

data class CustomerOrderDetailItemModel(
    val id: Long,
    val billingId: Long,
    val productCode: String?,
    val productName: String?,
    val count: Int,
    val price: Long,
    val productAmount: Long
)
