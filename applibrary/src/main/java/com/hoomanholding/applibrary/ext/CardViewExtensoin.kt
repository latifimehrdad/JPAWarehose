package com.hoomanholding.applibrary.ext

import android.graphics.Color
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.hoomanholding.applibrary.model.data.database.entity.BrandEntity

/**
 * Created by zar on 2/20/2023.
 */


//-------------------------------------------------------------------------------------------------- setBrandColor
@BindingAdapter("setBrandColor")
fun CardView.setBrandColor(brandEntity: BrandEntity?){
    val color = brandEntity?.let {
        Color.rgb(it.r, it.g, it.b)
    } ?: run {
        Color.rgb(200, 200, 200)
    }
    setCardBackgroundColor(color)
}
//-------------------------------------------------------------------------------------------------- setBrandColor