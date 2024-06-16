package com.hoomanholding.applibrary.model.data.response.basket.subuser

data class SubUserOrderModel(
    val id: String,
    val basketDate: String,
    val basketNumber: Int,
    val basketAmount: Long,
    val fullName: String,
    val items: List<SubUserOrderDetailModel>?
)
