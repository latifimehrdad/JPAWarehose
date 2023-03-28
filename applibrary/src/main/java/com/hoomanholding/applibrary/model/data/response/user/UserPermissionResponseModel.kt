package com.hoomanholding.applibrary.model.data.response.user

import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel


data class UserPermissionResponseModel (
        override val hasError: Boolean,
        override val message: String,
        val data : List<String>?
) : BaseResponseAbstractModel()