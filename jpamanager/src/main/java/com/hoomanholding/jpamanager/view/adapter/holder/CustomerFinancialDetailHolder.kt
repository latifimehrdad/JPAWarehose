package com.hoomanholding.jpamanager.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.jpamanager.databinding.ItemCustomerFinancialBinding
import com.hoomanholding.jpamanager.model.data.other.CustomerFinancialItemModel


/**
 * create by m-latifi on 4/18/2023
 */

class CustomerFinancialDetailHolder(
    val binding: ItemCustomerFinancialBinding,
    val click: Click
): RecyclerView.ViewHolder(binding.root) {

    interface Click{
        fun moreDetail(type: EnumCheckType?)
    }

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item : CustomerFinancialItemModel) {
        binding.item = item
        item.description?.let {
            binding.textViewDescription.visibility = View.VISIBLE
            binding.textViewDescription.setOnClickListener {
                click.moreDetail(item.description)
            }
        } ?: run { binding.textViewDescription.visibility = View.INVISIBLE }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


}