package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnReportModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.databinding.ItemReportPdfCustomerBillingReturnBinding

/**
 * Created by m-latifi on 6/8/2023.
 */

class BillingReturnPDFHolder(
    private val binding: ItemReportPdfCustomerBillingReturnBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: BillingAndReturnReportModel) {
        setValueToXml(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: BillingAndReturnReportModel) {
        binding.textViewFinalPrice.text = item.finalAmount.split()
        binding.textViewDiscountPrice.text = item.discount.split()
        binding.textViewAmountPrice.text = item.amount.split()
        binding.textViewDate.text = item.billingDate
        binding.textViewNumber.text = item.billingNumber.toString()
    }
    //---------------------------------------------------------------------------------------------- setValueToXml

}