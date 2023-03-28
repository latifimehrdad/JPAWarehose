package com.hoomanholding.applibrary.model.data.response.user

import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity


data class UserItemsModel(
    val items : List<UserInfoEntity>?
)