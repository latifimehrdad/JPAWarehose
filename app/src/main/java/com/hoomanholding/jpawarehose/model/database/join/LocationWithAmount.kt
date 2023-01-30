package com.hoomanholding.jpawarehose.model.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.hoomanholding.jpawarehose.model.database.entity.LocationAmountEntity
import com.hoomanholding.jpawarehose.model.database.entity.LocationEntity

data class LocationWithAmount(
    @Embedded val locationEntity: LocationEntity,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationId"
    )
    val locationAmount: LocationAmountEntity?,
)
