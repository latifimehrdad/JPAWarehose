package com.hoomanholding.applibrary.model.data.response.receipt

/**
 * Create by Mehrdad on 1/22/2023
 */
data class WarehouseReceiptProductLocationModel(
    val id : Int,
    val locationId : Int,
    val floor : Int,
    val corridor : Int,
    val column : Int,
    val row : Int,
    val shelf : Int,
    val location : String?
)
