package com.hoomanholding.applibrary.model.data.response.user

import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel


/**
 * Created by m-latifi on 11/26/2022.
 */

data class UserInfoResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : UserInfoEntity?
) : BaseResponseAbstractModel()