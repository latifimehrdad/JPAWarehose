package com.hoomanholding.jpawarehose.view.extension

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hoomanholding.jpawarehose.model.database.entity.ProductSaveReceiptEntity
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
                val count = value.cartonCount *
                        value.productWithBrandModel.productsEntity.tedadDarKarton +
                        value.packetCount
                "$count $title"
            }
            is ReceiptWithProduct -> {
                val count = value.receiptDetailEntity.tedad - value.receiptDetailEntity.placed
                "$count $title"
            }
            else -> ""
        }
    } ?: run { "" }
    text = temp
}