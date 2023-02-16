package com.hoomanholding.jpawarehose.view.extension

import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.data.database.join.LocationWithAmount
import com.hoomanholding.jpawarehose.model.data.database.join.ProductAmountModel
import com.hoomanholding.jpawarehose.model.data.database.join.ReceiptWithProduct
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
            is ProductAmountModel -> {
                val count = whenIsProductWithBrandAndAmountModel(value)
                "$count $title"
            }
            is ReceiptWithProduct -> {
                val count = whenIsReceiptWithProduct(value)
                "$count $title"
            }
            is LocationWithAmount -> {
                val count = whenIsLocationWithAmount(value)
                background = if (count == 0) {
                    setTextColor(context.getColor(R.color.white))
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.drawable_location_amount_zero
                    )
                } else {
                    setTextColor(context.getColor(R.color.primaryColorVariant))
                    AppCompatResources.getDrawable(context, R.drawable.drawable_location_amount)
                }
                "$count $title"
            }
            else -> ""
        }
    } ?: run { "" }
    text = temp
}


//-------------------------------------------------------------------------------------------------- whenIsProductWithBrandAndAmountModel
private fun whenIsProductWithBrandAndAmountModel(value : ProductAmountModel) : String {
    value.saveReceiptAmountEntity?.let {
        val count = it.cartonCount *
                value.productsEntity.tedadDarKarton +
                it.packetCount
        return count.toString()
    }
    return "0"
}
//-------------------------------------------------------------------------------------------------- whenIsProductWithBrandAndAmountModel


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