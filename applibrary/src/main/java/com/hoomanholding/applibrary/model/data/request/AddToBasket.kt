package com.hoomanholding.applibrary.model.data.request

/**
 * Created by m-latifi on 6/6/2023.
 */

data class AddToBasket(
    val ProductId: Int,
    val Count: Int,
    val Price: Long
)
