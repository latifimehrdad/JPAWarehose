package com.hoomanholding.applibrary.model.data.response.basket

/**
 * Created by m-latifi on 6/6/2023.
 */

data class DetailBasketModel(
    val id: Long,
    val basketId: String?,
    val productId: Int,
    val productCode: String?,
    val productName: String?,
    var count: Int,
    val price: Long,
    val detailAmount: Long,
    val productThumbnailImageName: String?,
    val isCash: Boolean,
    val maxCount: Int,
    val isSpecial: Boolean
)