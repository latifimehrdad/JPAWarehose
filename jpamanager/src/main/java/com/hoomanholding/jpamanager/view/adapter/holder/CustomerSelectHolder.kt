package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerBinding

/**
 * Created by m-latifi on 11/17/2022.
 */

class CustomerSelectHolder(
    private val binding: ItemCustomerBinding,
    private val click: Click
) : RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- Click
    interface Click {
        fun select(item : CustomerModel)
    }
    //---------------------------------------------------------------------------------------------- Click


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item : CustomerModel) {
        binding.item = item
        binding.root.setOnClickListener {
            click.select(item)
        }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}