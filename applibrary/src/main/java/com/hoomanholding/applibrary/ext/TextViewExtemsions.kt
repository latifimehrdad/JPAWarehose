package com.hoomanholding.applibrary.ext

import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.hoomanholding.applibrary.R
import com.hoomanholding.applibrary.model.data.database.entity.SupplierEntity
import com.hoomanholding.applibrary.model.data.database.join.*
import com.zar.core.tools.extensions.split
import com.zar.core.tools.extensions.toSolarDate
import java.time.LocalDateTime

/**
 * Create by Mehrdad on 1/27/2023
 */

@BindingAdapter("setTitle","setValue", "setSplitter")
fun TextView.setTitleAndValue(title : String, value : Any?, splitter: String){
    val temp = value?.let {
        when(value){
            is String -> "$title $splitter $value"
            is Long -> "$title $splitter $value"
            is Int -> "$title $splitter $value"
            is LocalDateTime -> "$title $splitter ${value.toSolarDate()?.getSolarDate()}"
            is ReceiptAmountWhitProductModel -> {
                val count = whenIsReceiptAmountWhitProductModel(value)
                "$count $splitter $title"
            }
            is ProductWithHistoryReceipt -> {
                val count = whenIsProductWithHistoryReceipt(value)
                "$count $splitter $title"
            }
            is ProductAmountModel -> {
                val count = whenIsProductWithBrandAndAmountModel(value)
                "$count $splitter $title"
            }
            is ReceiptWithProduct -> {
                val count = whenIsReceiptWithProduct(value)
                "$count $splitter $title"
            }
            is SupplierEntity -> {
                "$title $splitter ${value.nameTaminKonandeh}"
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
                "$count $splitter $title"
            }
            else -> ""
        }
    } ?: run { "" }
    text = temp
}

@BindingAdapter("setTitle","setValue", "setSplitter", "setLastTest")
fun TextView.setTitleAndValue(title : String, value : Any?, splitter: String, last: String){
    val temp = value?.let {
        when(value){
            is Long -> "$title $splitter ${value.split()} $last"
            else -> ""
        }
    } ?: run { "" }
    text = temp
}


//-------------------------------------------------------------------------------------------------- whenIsReceiptAmountWhitProductModel
private fun whenIsReceiptAmountWhitProductModel(value : ReceiptAmountWhitProductModel) : String {
    val count = value.saveReceiptAmountEntity.cartonCount *
            value.products.productsEntity.tedadDarKarton +
            value.saveReceiptAmountEntity.packetCount
    return count.toString()
}
//-------------------------------------------------------------------------------------------------- whenIsReceiptAmountWhitProductModel



//-------------------------------------------------------------------------------------------------- whenIsProductWithHistoryReceipt
private fun whenIsProductWithHistoryReceipt(value : ProductWithHistoryReceipt) : String {
    val count = value.amount.cartonCount *
            value.products.productsEntity.tedadDarKarton +
            value.amount.packetCount
    return count.toString()
}
//-------------------------------------------------------------------------------------------------- whenIsProductWithHistoryReceipt


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
