package com.hoomanholding.jpamanager.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemOrderBinding
import com.hoomanholding.applibrary.model.data.response.order.OrderModel


/**
 * create by m-latifi on 3/18/2023
 */

class OrderHolder(
    private val binding: ItemOrderBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {


    interface Click {
        fun orderDetail(item: OrderModel)
        fun customerFinancialDetail(customerId: Int)
    }


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: OrderModel) {
        binding.item = item
        binding.textViewDetail.setOnClickListener {
            click.orderDetail(item)
        }
        binding.textViewCustomerFinancial.setOnClickListener {
            click.customerFinancialDetail(item.customerId)
        }
        checkSelected(item)
        binding.root.setOnClickListener {
            item.select = !item.select
            checkSelected(item)
        }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- checkSelected
    private fun checkSelected(item: OrderModel){
        if (item.select)
            binding.viewSelect.visibility = View.VISIBLE
        else
            binding.viewSelect.visibility = View.GONE
    }
    //---------------------------------------------------------------------------------------------- checkSelected

}