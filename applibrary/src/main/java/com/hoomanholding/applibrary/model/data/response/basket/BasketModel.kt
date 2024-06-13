package com.hoomanholding.applibrary.model.data.response.basket

/**
 * Created by m-latifi on 6/6/2023.
 */

data class BasketModel(
    val id: String?,
    val basketDate: String?,
    val basketNumber: Int,
    val basketAmount: Long,
    val isExhibitionActive: Boolean,
    val items: List<DetailBasketModel>?
)
