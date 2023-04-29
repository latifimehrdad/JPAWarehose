package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerBalanceReportDatailBinding


/**
 * create by m-latifi on 4/29/2023
 */

class CustomerBalanceDetailHolder(
    private val binding: ItemCustomerBalanceReportDatailBinding
): RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerBalanceReportDetailModel) {
        binding.item = item
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}