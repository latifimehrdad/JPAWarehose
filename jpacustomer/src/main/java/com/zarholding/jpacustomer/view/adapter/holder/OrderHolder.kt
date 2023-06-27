package com.zarholding.jpacustomer.view.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setAmount
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemOrderBinding
import com.zarholding.jpacustomer.view.adapter.recycler.OrderAdapter


/**
 * create by m-latifi on 5/8/2023
 */

class OrderHolder(
    private val binding: ItemOrderBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {


    interface Click{
        fun click(position: Int, item: CustomerOrderModel)
    }

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerOrderModel, position: Int) {
        setValueToXml(item)
        setListener(item, position)
        setItemSelected(position)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind



    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: CustomerOrderModel) {
        val rial = binding.textViewOrderNumber.context.getString(R.string.rial)
        binding.textViewOrderNumber.text = item.orderNumber.toString()
        binding.textViewAmount.setAmount(
            value = item.orderAmount,
            rial
        )
        binding.textViewDiscount.setAmount(
            value = item.discount,
            rial
        )
        binding.textViewFinal.setAmount(
            value = item.finalAmount,
            rial
        )
        binding.textViewDate.text = item.orderDate
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: CustomerOrderModel, position: Int) {
        binding.root.setOnClickListener {
            click.click(position, item)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setItemSelected
    private fun setItemSelected(position: Int) {
        if (OrderAdapter.selectedPosition == position){
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.primaryColor))
            binding.imageViewShopping.setColorFilter(
                ContextCompat.getColor(binding.imageViewShopping.context, R.color.buttonTextColor),
                android.graphics.PorterDuff.Mode.SRC_IN)
            val color = binding.textViewAmount.context.getColor(R.color.buttonTextColor)
            binding.textViewOrderNumber.setTextColor(color)
            binding.textViewAmount.setTextColor(color)
            binding.textViewDiscount.setTextColor(color)
            binding.textViewFinal.setTextColor(color)
            binding.textViewDate.setTextColor(color)
        } else {
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.color4))
            binding.imageViewShopping.setColorFilter(
                ContextCompat.getColor(binding.imageViewShopping.context, R.color.textColor),
                android.graphics.PorterDuff.Mode.SRC_IN)
            val color = binding.textViewAmount.context.getColor(R.color.textColor)
            binding.textViewOrderNumber.setTextColor(color)
            binding.textViewAmount.setTextColor(color)
            binding.textViewDiscount.setTextColor(color)
            binding.textViewFinal.setTextColor(color)
            binding.textViewDate.setTextColor(color)
        }
    }
    //---------------------------------------------------------------------------------------------- setItemSelected

}