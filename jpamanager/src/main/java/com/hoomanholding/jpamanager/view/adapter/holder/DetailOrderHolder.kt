package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.order.DetailOrderModel
import com.hoomanholding.jpamanager.databinding.ItemOrderDetailBinding


/**
 * create by m-latifi on 4/10/2023
 */

class DetailOrderHolder(
    private val binding: ItemOrderDetailBinding
): RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: DetailOrderModel) {
        binding.item = item
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}