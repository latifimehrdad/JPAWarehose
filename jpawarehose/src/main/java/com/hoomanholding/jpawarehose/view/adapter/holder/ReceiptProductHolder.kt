package com.hoomanholding.jpawarehose.view.adapter.holder

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemReceiptProductBinding
import com.hoomanholding.applibrary.model.data.database.entity.BrandEntity
import com.hoomanholding.applibrary.model.data.database.join.LocationWithAmount
import com.hoomanholding.applibrary.model.data.database.join.ReceiptWithProduct
import com.hoomanholding.jpawarehose.tools.CreateDrawableByBrand
import com.hoomanholding.jpawarehose.view.adapter.ReceiptLocationAdapter


/**
 * Create by Mehrdad on 1/27/2023
 */
class ReceiptProductHolder(
    private val binding: ItemReceiptProductBinding,
    private val click: ReceiptLocationHolder.Click
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        var productPosition = -1
        var locationPosition = -1
    }

    //---------------------------------------------------------------------------------------------- bing
    fun bing(product: ReceiptWithProduct, position : Int) {
        binding.item = product
        val context = binding.root.context
        val brand = product.brandEntity
        val color = Color.rgb(brand.r, brand.g, brand.b)

        binding.color = color

        binding.recyclerViewLocation.visibility = View.GONE
        binding.viewDisable.visibility = View.VISIBLE

        if (productPosition == position) {
            binding.viewDisable.visibility = View.GONE
            setLocationAdapter(context,product.brandEntity, product.location)
        }

        binding.constraintLayoutContent.background =
            CreateDrawableByBrand().getCornerRadius(color)

        binding.textViewTotal.background =
            CreateDrawableByBrand().getRounded(context, color)

        binding.textViewRemaining.background =
            CreateDrawableByBrand().getRounded(context, color)

        binding.materialButtonAdd.backgroundTintList = ColorStateList.valueOf(color)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bing



    //---------------------------------------------------------------------------------------------- setLocationAdapter
    private fun setLocationAdapter(context : Context,
                                   brandEntity: BrandEntity,
                                   locations : List<LocationWithAmount>) {
        binding.recyclerViewLocation.visibility = View.VISIBLE
        val manager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false)
        val adapter = ReceiptLocationAdapter(brandEntity, locations, click)
        binding.recyclerViewLocation.layoutManager = manager
        binding.recyclerViewLocation.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setLocationAdapter

}