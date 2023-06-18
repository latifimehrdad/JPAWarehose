package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.databinding.ItemReportPdfCustomerBalanceBinding

/**
 * Created by m-latifi on 6/8/2023.
 */

class BalancePDFHolder(
    private val binding: ItemReportPdfCustomerBalanceBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerBalanceReportDetailModel) {
        setValueToXml(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: CustomerBalanceReportDetailModel) {
        binding.textViewBalance.text = item.balance.split()
        binding.textViewCredit.text = item.credit.split()
        binding.textViewDebit.text = item.debit.split()
        binding.textViewDate.text = item.transactionDate
        binding.textViewDescription.text = item.description.toString()
    }
    //---------------------------------------------------------------------------------------------- setValueToXml

}