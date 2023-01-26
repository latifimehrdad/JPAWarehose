package com.hoomanholding.jpawarehose.view.extension

import android.widget.TextView
import androidx.databinding.BindingAdapter
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
            is Int -> "$title $value"
            is LocalDateTime -> "$title ${value.toSolarDate()?.getSolarDate()}"
            else -> ""
        }
    } ?: run { "" }
    text = temp
}