package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Mehrdad on 1/22/2023
 */
@Entity(tableName = "Receipt")
data class ReceiptEntity(
    val shomarehForm : Int,
    val shomarehBargeh : String?,
    val tarikhForm : String?,
    val codeTaminKonandeh : Int,
    val idTaminKonandeh : Int,
    val nameTaminKonandeh : String?,
    val tozihat : String?
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}
