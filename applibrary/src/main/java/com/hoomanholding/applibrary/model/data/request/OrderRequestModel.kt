package com.hoomanholding.applibrary.model.data.request


/**
 * create by m-latifi on 3/18/2023
 */

data class OrderRequestModel(
    val fromDate: String,
    val toDate: String,
    val customerId: Int,
    val visitorId: Int,
    val state: Int
)
