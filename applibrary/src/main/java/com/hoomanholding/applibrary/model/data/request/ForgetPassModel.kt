package com.hoomanholding.applibrary.model.data.request

import com.hoomanholding.applibrary.model.data.enums.EnumSystemType

/**
 * Created by m-latifi on 7/13/2023.
 */

data class ForgetPassModel(
    val systemTypesEnum: EnumSystemType,
    val userName: String,
    val androidId: String
)
