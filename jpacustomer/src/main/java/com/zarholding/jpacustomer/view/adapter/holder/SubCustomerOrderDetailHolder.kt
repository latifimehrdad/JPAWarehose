package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.basket.subuser.SubUserOrderDetailModel
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderDetailModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemSubCustomerOrderDetailBinding

class SubCustomerOrderDetailHolder(
    private val binding: ItemSubCustomerOrderDetailBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: SubUserOrderDetailModel) {
        setValueToXml(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: SubUserOrderDetailModel) {
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
        binding.textViewAmount.setTitleAndValue(
            title = binding.textViewAmount.context.getString(R.string.totalAmount),
            splitter = binding.textViewAmount.context.getString(R.string.colon),
            value = item.detailAmount.split()
        )
        binding.textViewFi.setTitleAndValue(
            title = binding.textViewFi.context.getString(R.string.fi),
            splitter = binding.textViewFi.context.getString(R.string.colon),
            value = item.price.split()
        )
    }
    //---------------------------------------------------------------------------------------------- setValueToXml

}