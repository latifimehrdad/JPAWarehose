package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerHyperlinkBinding


/**
 * create by m-latifi on 4/18/2023
 */

class CustomerHyperLinkHolder(
    private val binding: ItemCustomerHyperlinkBinding
): RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerFinancialDetailModel){
        binding.item = item
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind
}