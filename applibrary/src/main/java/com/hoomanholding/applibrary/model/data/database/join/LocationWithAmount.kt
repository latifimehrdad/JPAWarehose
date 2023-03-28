package com.hoomanholding.applibrary.model.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.applibrary.model.data.database.entity.LocationAmountEntity
import com.hoomanholding.applibrary.model.data.database.entity.LocationEntity

data class LocationWithAmount(
    @Embedded val locationEntity: LocationEntity,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationId"
    )
    val locationAmount: LocationAmountEntity?,
)
