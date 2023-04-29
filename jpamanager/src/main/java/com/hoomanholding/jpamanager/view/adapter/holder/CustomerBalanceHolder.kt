package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerBalanceReportBinding


/**
 * create by m-latifi on 4/29/2023
 */

class CustomerBalanceHolder(
    private val binding: ItemCustomerBalanceReportBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {

    interface Click {
        fun reportDetail(item: CustomerBalanceReportModel)
    }


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerBalanceReportModel) {
        binding.item = item
        binding.textViewDetail.setOnClickListener {
            click.reportDetail(item)
        }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}