package com.hoomanholding.applibrary.tools

import android.content.Context
import android.graphics.drawable.Drawable
import com.hoomanholding.applibrary.R
import top.defaults.drawabletoolbox.DrawableBuilder

/**
 * Create by Mehrdad on 1/27/2023
 */
class CreateDrawableByBrand {

    //---------------------------------------------------------------------------------------------- getRounded
    fun getCornerRadius(color : Int): Drawable = DrawableBuilder()
        .rectangle()
        .cornerRadius(30)
        .hairlineBordered()
        .strokeColor(color)
        .build()
    //---------------------------------------------------------------------------------------------- getRounded


    //---------------------------------------------------------------------------------------------- getRounded
    fun getRounded(context : Context, color : Int): Drawable = DrawableBuilder()
        .rectangle()
        .rounded()
        .hairlineBordered()
        .solidColor(
            context.resources.getColor(
                R.color.primaryColorVariant,
                context.theme
            )
        )
        .strokeColor(color)
        .build()
    //---------------------------------------------------------------------------------------------- getRounded
}