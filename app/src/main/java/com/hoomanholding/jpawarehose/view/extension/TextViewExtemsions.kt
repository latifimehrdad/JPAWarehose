package com.hoomanholding.jpawarehose.view.extension

import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.database.entity.ProductSaveReceiptEntity
import com.hoomanholding.jpawarehose.model.database.join.LocationWithAmount
import com.hoomanholding.jpawarehose.model.database.join.ProductWithBrandModel
import com.hoomanholding.jpawarehose.model.database.join.ReceiptWithProduct
import com.zar.core.tools.extensions.toSolarDate
import java.time.LocalDateTime

/**
 * Create by Mehrdad on 1/27/2023
 */

@BindingAdapter("setTitle","setValue")
fun TextView.setTitleAndValue(title : String, value : Any?){
    val temp = value?.let {
        when(value){
            is String -> "$title $value"
            is Long -> "$title $value"
            is Int -> "$value $title"
            is LocalDateTime -> "$title ${value.toSolarDate()?.getSolarDate()}"
            is ProductSaveReceiptEntity -> {
                val count = whenIsProductSaveReceiptEntity(value)
                "$count $title"
            }
            is ReceiptWithProduct -> {
                val count = whenIsReceiptWithProduct(value)
                "$count $title"
            }
            is LocationWithAmount -> {
                val count = whenIsLocationWithAmount(value)
                if (count == 0)
                    background = AppCompatResources.
                    getDrawable(context, R.drawable.drawable_location_amount_zero)
                else
                    background = AppCompatResources.
                    getDrawable(context, R.drawable.drawable_location_amount)
                "$count $title"
            }
            else -> ""
        }
    } ?: run { "" }
    text = temp
}


//-------------------------------------------------------------------------------------------------- whenIsProductSaveReceiptEntity
private fun whenIsProductSaveReceiptEntity(value : ProductSaveReceiptEntity) : String {
    val count = value.cartonCount *
            value.productWithBrandModel.productsEntity.tedadDarKarton +
            value.packetCount
    return count.toString()
}
//-------------------------------------------------------------------------------------------------- whenIsProductSaveReceiptEntity


//-------------------------------------------------------------------------------------------------- whenIsReceiptWithProduct
private fun whenIsReceiptWithProduct(value : ReceiptWithProduct) : String {
    var countReplace = 0
    for(item in value.location) {
        item.locationAmount?.let {
            countReplace += it.amount
        }
    }
    val count = value.receiptDetailEntity.tedad - countReplace
    return count.toString()
}
//-------------------------------------------------------------------------------------------------- whenIsReceiptWithProduct


//-------------------------------------------------------------------------------------------------- whenIsLocationWithAmount
private fun whenIsLocationWithAmount(value : LocationWithAmount) : Int {
    return value.locationAmount?.amount ?: 0
}
//-------------------------------------------------------------------------------------------------- whenIsLocationWithAmount