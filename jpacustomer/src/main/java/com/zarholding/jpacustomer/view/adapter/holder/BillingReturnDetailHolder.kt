package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnDetailReportModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemReportBillingReturnDetailBinding

/**
 * Created by m-latifi on 6/8/2023.
 */

class BillingReturnDetailHolder(
    private val binding: ItemReportBillingReturnDetailBinding
): RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: BillingAndReturnDetailReportModel) {
        setValueToXml(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: BillingAndReturnDetailReportModel) {
        binding.textViewName.text = item.productName
        binding.textViewFinalPrice.text = item.price.split()
        binding.textViewCount.setTitleAndValue(
            title = item.count.toString(),
            splitter = binding.textViewCount.context.getString(R.string.space),
            value = binding.textViewCount.context.getString(R.string.pieces)
        )
    }
    //---------------------------------------------------------------------------------------------- setValueToXml

}