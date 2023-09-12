package com.hoomanholding.applibrary.model.data.response.user

data class UserModel(
    val id: Long,
    val customerCode: Long,
    val customerGroupId: Long,
    val customerGroupName: String?,
    val customerName: String?,
    val routeId: Long,
    val routeName: String?,
    val storeName: String?,
    val visitorId: String?,
    val visitorName: String?,
    var select: Boolean = false
)