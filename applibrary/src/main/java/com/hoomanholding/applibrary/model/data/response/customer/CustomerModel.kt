package com.hoomanholding.applibrary.model.data.response.customer


/**
 * create by m-latifi on 4/5/2023
 */

data class CustomerModel(
    val id: Int,
    val customerCode: Int,
    val customerName: String?,
    val storeName: String?,
    val routeId: Int,
    val routeName: String?,
    val visitorId: String?,
    val visitorName: String?,
    val customerGroupId: Int,
    val customerGroupName: String?
)
