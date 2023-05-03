package com.hoomanholding.applibrary.model.data.request

import com.hoomanholding.applibrary.model.data.enums.EnumSystemType

/**
 * Created by m-latifi on 11/9/2022.
 */

data class LoginRequestModel(
    val userName : String,
    val password : String,
    val SystemType: EnumSystemType,
    val AndroidId: String
)
