package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity


@Entity(tableName = "LocationAmount", primaryKeys = ["locationId", "productId"])
data class LocationAmountEntity(
    val locationId : Long,
    val productId : Long,
    var amount : Int
)
