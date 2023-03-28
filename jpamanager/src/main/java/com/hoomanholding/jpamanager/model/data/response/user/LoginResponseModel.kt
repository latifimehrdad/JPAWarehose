package com.hoomanholding.jpamanager.model.data.response.user

import com.hoomanholding.jpamanager.model.data.response.BaseResponseAbstractModel


/**
 * Created by m-latifi on 11/9/2022.
 */

data class LoginResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data: String?
) : BaseResponseAbstractModel()
