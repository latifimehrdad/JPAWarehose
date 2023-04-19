package com.hoomanholding.applibrary.model.data.request

import com.hoomanholding.applibrary.model.data.enums.EnumState


/**
 * create by m-latifi on 4/5/2023
 */

data class OrderToggleStateRequest(
    val state: EnumState,
    val description: String?,
    val orderIds: List<Int>,
    val rejectReasonId: Int
)
