package com.hoomanholding.applibrary.model.data.response.critic

/**
 * Created by m-latifi on 8/2/2023.
 */

data class CriticDetailModel(
    val headerId: Long,
    val id: Long,
    val message: String?,
    val isAdmin: Boolean,
    val date: String,
    val time: String,
    val sending: Boolean = false
)
