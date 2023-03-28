package com.hoomanholding.jpamanager.model.data.request


/**
 * create by m-latifi on 3/18/2023
 */

data class OrderRequestModel(
    val fromDate: String,
    val toDate: String,
    val customerId: Long,
    val visitorId: Long
)
