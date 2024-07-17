package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.report.CustomerReturnBasketReportItemModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemReportCustomerBasketReturnDetailBinding


class ReportCustomerBasketReturnDetailHolder(
    private val binding: ItemReportCustomerBasketReturnDetailBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerReturnBasketReportItemModel) {
        setValueToXml(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: CustomerReturnBasketReportItemModel) {
        binding.textViewProductCode.setTitleAndValue(
            title = binding.textViewProductCode.context.getString(R.string.productCode),
            splitter = binding.textViewProductCode.context.getString(R.string.colon),
            value = item.productCode
        )
        binding.textViewProductName.setTitleAndValue(
            title = binding.textViewProductName.context.getString(R.string.productName),
            splitter = binding.textViewProductName.context.getString(R.string.colon),
            value = item.productName
        )
        binding.textViewCount.setTitleAndValue(
            title = binding.textViewCount.context.getString(R.string.count),
            splitter = binding.textViewCount.context.getString(R.string.colon),
            value = item.count.split()
        )
    }
    //---------------------------------------------------------------------------------------------- setValueToXml

}