package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemOrderBinding
import com.hoomanholding.jpamanager.model.data.response.order.OrderModel


/**
 * create by m-latifi on 3/18/2023
 */

class OrderHolder(
    private val binding: ItemOrderBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {


    interface Click {
        fun orderDetail(item: OrderModel)
    }


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: OrderModel) {
        binding.item = item
        binding.root.setOnClickListener { click.orderDetail(item) }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}