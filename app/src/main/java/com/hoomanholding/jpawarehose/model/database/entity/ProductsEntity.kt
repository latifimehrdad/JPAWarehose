package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Mehrdad on 1/18/2023
 */

@Entity(tableName = "Product")
data class ProductsEntity(
    val codeKala : String?,
    val nameKala : String?,
    val brandId : Int,
    val nameBrand : String?,
    val nameBrandKamel : String?,
    val productGroup1Id : Int,
    val productGroup2Id : Int,
    val productGroup3Id : Int,
    val productGroup4Id : Int,
    val productGroup1Name : String?,
    val productGroup2Name : String?,
    val productGroup3Name : String?,
    val productGroup4Name : String?,
    val tedadDarKarton : Int,
    val tedadDarBasteh : Int
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}
