package com.hoomanholding.jpamanager.model.data.other

import com.hoomanholding.applibrary.model.data.enums.EnumCheckType


/**
 * create by m-latifi on 4/18/2023
 */

data class CustomerFinancialItemModel(
    val title: String,
    val value: Any?,
    val description: EnumCheckType?,
    val lastTitle: String
)