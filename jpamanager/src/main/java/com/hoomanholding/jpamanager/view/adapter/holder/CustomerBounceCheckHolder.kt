package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerBounceCheckReportModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerBounceCheckBinding


/**
 * create by m-latifi on 5/1/2023
 */

class CustomerBounceCheckHolder(
    private val binding: ItemCustomerBounceCheckBinding
): RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- CustomerBounceCheckReportModel
    fun bind(item: CustomerBounceCheckReportModel){
        binding.item = item
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- CustomerBounceCheckReportModel

}