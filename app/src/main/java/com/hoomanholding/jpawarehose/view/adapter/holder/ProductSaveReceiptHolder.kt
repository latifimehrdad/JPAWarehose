package com.hoomanholding.jpawarehose.view.adapter.holder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemProductSaveReceiptBinding
import com.hoomanholding.jpawarehose.model.database.entity.ProductSaveReceiptEntity
import com.hoomanholding.jpawarehose.utility.CreateDrawableByBrand


/**
 * Create by Mehrdad on 1/27/2023
 */
class ProductSaveReceiptHolder(
    private val binding: ItemProductSaveReceiptBinding,
    private val click: Click
) :
    RecyclerView.ViewHolder(binding.root) {


    interface Click {
        fun addCarton(position : Int)
        fun addPacket(position : Int)
        fun minusCarton(position : Int)
        fun minusPacket(position : Int)
    }

    //---------------------------------------------------------------------------------------------- bing
    fun bing(product: ProductSaveReceiptEntity, position : Int) {
        binding.item = product

        setListener(position)

        if (product.cartonCount == 0 && product.packetCount == 0) {
            binding.materialButtonAdd.visibility = View.VISIBLE
            binding.textViewTotal.visibility = View.GONE
            binding.constraintLayoutCarton.visibility = View.GONE
            binding.constraintLayoutPacket.visibility = View.GONE
        } else {
            binding.materialButtonAdd.visibility = View.GONE
            binding.textViewTotal.visibility = View.VISIBLE
            binding.constraintLayoutCarton.visibility = View.VISIBLE
            binding.constraintLayoutPacket.visibility = View.VISIBLE
        }

        val context = binding.root.context
        val brand = product.productWithBrandModel.brandEntity
        val color = brand?.let {
            Color.rgb(it.r, it.g, it.b)
        } ?: run {
            Color.rgb(200, 200, 200)
        }

        binding.constraintLayoutContent.background =
            CreateDrawableByBrand().getCornerRadius(color)
        binding.textViewTotal.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewTotal.setTextColor(color)

        setCartonColor(color)
        setPacketColor(color)
        binding.materialButtonAdd.backgroundTintList = ColorStateList.valueOf(color)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bing


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(position : Int) {
        binding.materialButtonAdd.setOnClickListener {
            binding.materialButtonAdd.visibility = View.GONE
            binding.textViewTotal.visibility = View.VISIBLE
            binding.constraintLayoutCarton.visibility = View.VISIBLE
            binding.constraintLayoutPacket.visibility = View.VISIBLE
        }

        binding.textViewCartonAdd.setOnClickListener {
            click.addCarton(position)
        }

        binding.textViewCartonMinus.setOnClickListener {
            click.minusCarton(position)
        }

        binding.textViewPacketAdd.setOnClickListener {
            click.addPacket(position)
        }

        binding.textViewPacketMinus.setOnClickListener {
            click.minusPacket(position)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setCartonColor
    private fun setCartonColor(color : Int) {
        val context = binding.root.context
        binding.constraintLayoutCarton.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewCartonAdd.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewCartonAdd.setTextColor(color)
        binding.textViewCartonMinus.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewCartonMinus.setTextColor(color)
    }
    //---------------------------------------------------------------------------------------------- setCartonColor


    //---------------------------------------------------------------------------------------------- setPacketColor
    private fun setPacketColor(color : Int) {
        val context = binding.root.context
        binding.constraintLayoutPacket.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewPacketAdd.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewPacketAdd.setTextColor(color)
        binding.textViewPacketMinus.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewPacketMinus.setTextColor(color)
    }
    //---------------------------------------------------------------------------------------------- setPacketColor

}