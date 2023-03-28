package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemHomeBinding


/**
 * create by m-latifi on 3/7/2023
 */

class HomeItemHolder(
    private val binding: ItemHomeBinding
): RecyclerView.ViewHolder(binding.root)  {


    //---------------------------------------------------------------------------------------------- bind
    fun bind() {
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}