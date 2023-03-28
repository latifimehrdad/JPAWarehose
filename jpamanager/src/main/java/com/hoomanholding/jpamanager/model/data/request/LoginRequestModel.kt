package com.hoomanholding.jpamanager.model.data.request

/**
 * Created by m-latifi on 11/9/2022.
 */

data class LoginRequestModel(
    val userName : String,
    val password : String,
    val SystemType: String,
    val AndroidId: String
)
