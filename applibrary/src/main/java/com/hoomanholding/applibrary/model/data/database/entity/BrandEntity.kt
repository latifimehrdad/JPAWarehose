package com.hoomanholding.applibrary.model.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Mehrdad on 1/22/2023
 */
@Entity(tableName = "Brand")
data class BrandEntity(
    val brandNickName : String?,
    val brandName : String?,
    val r : Int,
    val g : Int,
    val b : Int
) {
    @PrimaryKey(autoGenerate = false)
    var id : Long = 0
}