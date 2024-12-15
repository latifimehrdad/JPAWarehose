package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportModel
import com.hoomanholding.jpamanager.R
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
        setValues(item = item)
        binding.textViewDetail.setOnClickListener {
            click.reportDetail(item)
        }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValues
    private fun setValues(item: CustomerBalanceReportModel) {
        val context = binding.root.context
        binding.textViewCustomerCode.setTitleAndValue(
            title = context.getString(R.string.customerCode),
            splitter = context.getString(R.string.colon),
            value = item.customerCode
        )
        binding.textViewCustomerName.setTitleAndValue(
            title = context.getString(R.string.customerName),
            splitter = context.getString(R.string.colon),
            value = item.customerName
        )
        binding.textViewStoreName.setTitleAndValue(
            title = context.getString(R.string.shopName),
            splitter = context.getString(R.string.colon),
            value = item.storeName
        )
        binding.textViewVisitorName.setTitleAndValue(
            title = context.getString(R.string.visitorName),
            splitter = context.getString(R.string.colon),
            value = item.visitorName
        )
        binding.textViewBalance.setTitleAndValue(
            title = context.getString(R.string.remained),
            splitter = context.getString(R.string.colon),
            value = item.balance,
            last = context.getString(R.string.rial)
        )
    }
    //---------------------------------------------------------------------------------------------- setValues
}