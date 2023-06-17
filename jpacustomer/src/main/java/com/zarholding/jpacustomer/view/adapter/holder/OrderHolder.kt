package com.zarholding.jpacustomer.view.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.zar.core.tools.extensions.split
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
        setValueToXml(item)
        setListener(item, position)
        setItemSelected(position)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind



    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: CustomerOrderModel) {
        binding.textViewNumber.text = item.orderNumber.toString()
        binding.textViewAmount.text = item.orderAmount.split()
        binding.textViewDate.text = item.orderDate
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: CustomerOrderModel, position: Int) {
        binding.root.setOnClickListener {
            click.click(position, item)
        }
        binding.buttonShowOrderDetail.setOnClickListener {
            click.clickDetail(item)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setItemSelected
    private fun setItemSelected(position: Int) {
        if (OrderAdapter.selectedPosition == position){
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.a_cardView4))
            binding.buttonShowOrderDetail.backgroundTintList =
                binding.buttonShowOrderDetail.context.getColorStateList(R.color.a_orderButtonSelect)
            binding.buttonShowOrderDetail
                .setTextColor(binding.buttonShowOrderDetail.context.getColor(R.color.a_cardView4))
            binding.imageViewShopping.setColorFilter(
                ContextCompat.getColor(binding.imageViewShopping.context, R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN)
            binding.textViewNumber
                .setTextColor(binding.textViewNumber.context.getColor(R.color.white))
        } else {
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.a_cardView5))
            binding.buttonShowOrderDetail.backgroundTintList =
                binding.buttonShowOrderDetail.context.getColorStateList(R.color.a_cardView3)
            binding.buttonShowOrderDetail
                .setTextColor(binding.buttonShowOrderDetail.context.getColor(R.color.a_cardView5))
            binding.imageViewShopping.setColorFilter(
                ContextCompat.getColor(binding.imageViewShopping.context, R.color.primaryColor),
                android.graphics.PorterDuff.Mode.SRC_IN)
            binding.textViewNumber
                .setTextColor(binding.textViewNumber.context.getColor(R.color.primaryColor))
        }
    }
    //---------------------------------------------------------------------------------------------- setItemSelected

}