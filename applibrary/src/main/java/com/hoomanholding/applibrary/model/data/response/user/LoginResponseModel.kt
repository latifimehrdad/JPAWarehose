package com.hoomanholding.applibrary.model.data.response.user

import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel


/**
 * Created by m-latifi on 11/9/2022.
 */

data class LoginResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data: String?
) : BaseResponseAbstractModel()
