package com.hoomanholding.jpamanager.ext

import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.zar.core.tools.extensions.toSolarDate
import java.time.LocalDateTime

/**
 * Create by Mehrdad on 1/27/2023
 */

//-------------------------------------------------------------------------------------------------- setTitleAndValue
@BindingAdapter("setTitle","setValue", "setSplitter")
fun TextView.setTitleAndValue(title : String, value : Any?, splitter: String){
    val temp = value?.let {
        when(value){
            is String -> "$title $splitter $value"
            is Long -> "$title $splitter $value"
            is Int -> "$value $splitter $title"
            is LocalDateTime -> "$title $splitter ${value.toSolarDate()?.getSolarDate()}"
            else -> ""
        }
    } ?: run { "" }
    text = temp
}
//-------------------------------------------------------------------------------------------------- setTitleAndValue
