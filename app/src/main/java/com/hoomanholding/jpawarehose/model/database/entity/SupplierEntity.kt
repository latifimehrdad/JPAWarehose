package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Mehrdad on 1/18/2023
 */
@Entity(tableName = "Supplier")
data class SupplierEntity(
    val codeTaminKonandeh : Int,
    val nameTaminKonandeh : String?
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}
