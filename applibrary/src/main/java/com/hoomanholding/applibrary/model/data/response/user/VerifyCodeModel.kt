package com.hoomanholding.applibrary.model.data.response.user


/**
 * create by m-latifi on 5/4/2023
 */

data class VerifyCodeModel(
    val isVerificationCorrect: Boolean,
    val isforceChangePassword: Boolean
)