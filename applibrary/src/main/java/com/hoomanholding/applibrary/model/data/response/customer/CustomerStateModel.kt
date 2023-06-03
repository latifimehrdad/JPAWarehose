package com.hoomanholding.applibrary.model.data.response.customer

/**
 * Created by m-latifi on 6/3/2023.
 */

data class CustomerStateModel(
    val billingAmount: Long,
    val billingCount: Int,
    val returnAmount: Long,
    val returnCount: Int,
    val remain: Long,
    val dependentCheckCount: Int,
    val dependentCheckAmount: Long,
    val notDueCheckList: List<NotDueCheckModel>?
)
