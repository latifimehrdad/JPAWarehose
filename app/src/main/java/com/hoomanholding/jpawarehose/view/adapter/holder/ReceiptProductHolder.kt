package com.hoomanholding.jpawarehose.view.adapter.holder

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemReceiptProductBinding
import com.hoomanholding.jpawarehose.model.database.join.ReceiptWithProduct
import com.hoomanholding.jpawarehose.utility.CreateDrawableByBrand


/**
 * Create by Mehrdad on 1/27/2023
 */
class ReceiptProductHolder(
    private val binding: ItemReceiptProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        var productPosition = -1
    }

    //---------------------------------------------------------------------------------------------- bing
    fun bing(product: ReceiptWithProduct, position : Int) {
        binding.item = product
        val context = binding.root.context
        if (productPosition == position)
            binding.constraintLayoutParent.alpha = 1.0f
        else
            binding.constraintLayoutParent.alpha = 0.5f

        val brand = product.brandEntity
        val color = Color.rgb(brand.r, brand.g, brand.b)

        binding.constraintLayoutContent.background =
            CreateDrawableByBrand().getCornerRadius(color)
        binding.textViewTotal.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewTotal.setTextColor(color)

        binding.textViewRemaining.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewRemaining.setTextColor(color)

        binding.materialButtonAdd.backgroundTintList = ColorStateList.valueOf(color)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bing


}