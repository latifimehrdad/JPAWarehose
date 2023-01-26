package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Mehrdad on 1/26/2023
 */

@Entity(tableName = "Location")
data class LocationEntity(
    val productId : Long,
    val floor : Int,
    val corridor : Int,
    val column : Int,
    val shelf : Int,
    val location : String?
) {
    @PrimaryKey(autoGenerate = false)
    var locationId : Long = 0
}
