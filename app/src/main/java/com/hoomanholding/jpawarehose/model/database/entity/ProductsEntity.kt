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
    val brandId : Long,
    val nameBrand : String?,
    val nameBrandKamel : String?,
    val productGroup1Id : Long,
    val productGroup2Id : Long,
    val productGroup3Id : Long,
    val productGroup4Id : Long,
    val productGroup1Name : String?,
    val productGroup2Name : String?,
    val productGroup3Name : String?,
    val productGroup4Name : String?,
    val tedadDarKarton : Int,
    val tedadDarBasteh : Int,
    var cartonCount : Int = 0,
    var packetCount : Int = 0
) {
    @PrimaryKey(autoGenerate = false)
    var id : Long = 0
}
