package com.hoomanholding.jpawarehose.model.data.response.user

import com.hoomanholding.jpawarehose.model.data.response.BaseResponseAbstractModel
import com.hoomanholding.jpawarehose.model.database.entity.UserInfoEntity

/**
 * Created by m-latifi on 11/26/2022.
 */

data class UserInfoResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : UserInfoEntity?
) : BaseResponseAbstractModel()
