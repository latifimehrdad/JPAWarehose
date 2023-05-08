package com.zarholding.jpacustomer.view.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
        fun clickDetail(item: CustomerOrderModel)

    }

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerOrderModel, position: Int) {
        binding.item = item
        binding.root.setOnClickListener {
            click.click(position, item)
        }
        binding.buttonShowOrderDetail.setOnClickListener {
            click.clickDetail(item)
        }
        setItemSelected(position)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setItemSelected
    private fun setItemSelected(position: Int) {
        if (OrderAdapter.selectedPosition == position){
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.primaryColor))
            binding.buttonShowOrderDetail.backgroundTintList =
                binding.buttonShowOrderDetail.context.getColorStateList(R.color.cardViewMenuColor)
            binding.buttonShowOrderDetail
                .setTextColor(binding.buttonShowOrderDetail.context.getColor(R.color.primaryColor))
            binding.imageViewShopping.setColorFilter(
                ContextCompat.getColor(binding.imageViewShopping.context, R.color.cardViewMenuColor),
                android.graphics.PorterDuff.Mode.SRC_IN)
            binding.textViewNumber
                .setTextColor(binding.textViewNumber.context.getColor(R.color.cardViewMenuColor))
        } else {
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.cardViewMenuColor))
            binding.buttonShowOrderDetail.backgroundTintList =
                binding.buttonShowOrderDetail.context.getColorStateList(R.color.primaryColor)
            binding.buttonShowOrderDetail
                .setTextColor(binding.buttonShowOrderDetail.context.getColor(R.color.cardViewMenuColor))
            binding.imageViewShopping.setColorFilter(
                ContextCompat.getColor(binding.imageViewShopping.context, R.color.primaryColor),
                android.graphics.PorterDuff.Mode.SRC_IN)
            binding.textViewNumber
                .setTextColor(binding.textViewNumber.context.getColor(R.color.primaryColor))
        }
    }
    //---------------------------------------------------------------------------------------------- setItemSelected

}