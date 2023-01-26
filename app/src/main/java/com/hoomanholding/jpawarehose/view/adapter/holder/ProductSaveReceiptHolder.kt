package com.hoomanholding.jpawarehose.view.adapter.holder

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.ItemProductSaveReceiptBinding
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.database.join.ProductWithBrandModel
import top.defaults.drawabletoolbox.DrawableBuilder


/**
 * Create by Mehrdad on 1/27/2023
 */
class ProductSaveReceiptHolder(
    private val binding: ItemProductSaveReceiptBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- bing
    fun bing(product: ProductWithBrandModel) {
        binding.item = product
        val brand = product.brandEntity
        val color = brand?.let {
            Color.rgb(it.r,it.g,it.b)
        } ?: run {
            Color.rgb(200,200,200)
        }
        val drawable: Drawable = DrawableBuilder()
            .rectangle()
            .cornerRadius(30)
            .hairlineBordered()
            .strokeColor(color)
            .build()
        binding.constraintLayoutContent.background = drawable
        binding.materialButtonAdd.backgroundTintList  = ColorStateList.valueOf(color)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bing


}