package com.hoomanholding.jpamanager.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter


/**
 * create by m-latifi on 3/6/2023
 */

//-------------------------------------------------------------------------------------------------- ImageView.setItemIcon
@BindingAdapter("setItemIcon")
fun ImageView.setItemIcon(icon: Int) {
    setImageResource(icon)
}
//-------------------------------------------------------------------------------------------------- ImageView.setItemIcon
