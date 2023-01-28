package com.hoomanholding.jpawarehose.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hoomanholding.jpawarehose.model.database.join.ProductWithBrandModel

/**
 * Create by Mehrdad on 1/27/2023
 */

/*
* این جدول رو برای این ساختم که تعداد کارتن و بسته رو داخل جدول کالا ذخیره نکنم و همچنین برای نمایش نیاز به برند اون کالا هم بود
*/

@Entity(tableName = "ProductSaveReceipt")
data class ProductSaveReceiptEntity(
    val productWithBrandModel : ProductWithBrandModel,
    val search : String,
    var cartonCount : Int = 0,
    var packetCount : Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}