package com.hoomanholding.applibrary.model.data.response.critic

/**
 * Created by m-latifi on 8/2/2023.
 */

data class CriticModel(
    val id: Long,
    val tittle: String?,
    val isFinished: Boolean,
    val rate: Int,
    val createdate: String,
    val txtStatus: String
)
