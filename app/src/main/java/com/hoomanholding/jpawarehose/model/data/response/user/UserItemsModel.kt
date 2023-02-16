package com.hoomanholding.jpawarehose.model.data.response.user

import com.hoomanholding.jpawarehose.model.data.database.entity.UserInfoEntity


data class UserItemsModel(
    val items : List<UserInfoEntity>?
)