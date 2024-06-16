package com.hoomanholding.applibrary.model.data.response.basket.subuser

data class SubUserOrderDetailModel(
    val id: Long,
    val basketId: String,
    val productId: Long,
    val productCode: String,
    val productName: String,
    val count: Int,
    val price: Long,
    val detailAmount: Long,
    val isCash: Boolean
)
