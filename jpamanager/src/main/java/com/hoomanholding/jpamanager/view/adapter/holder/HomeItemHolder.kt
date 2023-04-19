package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemHomeBinding
import com.hoomanholding.jpamanager.model.data.other.HomeReportItemModel


/**
 * create by m-latifi on 3/7/2023
 */

class HomeItemHolder(
    private val binding: ItemHomeBinding
): RecyclerView.ViewHolder(binding.root)  {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: HomeReportItemModel) {
        binding.item = item
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}