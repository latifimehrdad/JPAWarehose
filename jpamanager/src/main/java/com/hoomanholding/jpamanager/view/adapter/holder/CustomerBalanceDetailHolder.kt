package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.ItemCustomerBalanceReportDatailBinding


/**
 * create by m-latifi on 4/29/2023
 */

class CustomerBalanceDetailHolder(
    private val binding: ItemCustomerBalanceReportDatailBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerBalanceReportDetailModel) {
        setValue(item = item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValue
    fun setValue(item: CustomerBalanceReportDetailModel) {
        val context = binding.root.context
        binding.textViewCreditor.setTitleAndValue(
            title = context.getString(R.string.creditor),
            splitter = context.getString(R.string.colon),
            value = item.credit,
            last = context.getString(R.string.rial)
        )
        binding.textViewDebit.setTitleAndValue(
            title = context.getString(R.string.debit),
            splitter = context.getString(R.string.colon),
            value = item.debit,
            last = context.getString(R.string.rial)
        )
        binding.textViewDecsription.setTitleAndValue(
            title = context.getString(R.string.description),
            splitter = context.getString(R.string.colon),
            value = item.description
        )
        binding.textViewBalance.setTitleAndValue(
            title = context.getString(R.string.remained),
            splitter = context.getString(R.string.colon),
            value = item.balance,
            last = context.getString(R.string.rial)
        )
        binding.textViewDate.setTitleAndValue(
            title = context.getString(R.string.date),
            splitter = context.getString(R.string.colon),
            value = item.transactionDate
        )
        if (item.balance > 0)
            binding.textViewBalance.setTextColor(
                binding.textViewBalance.context.getColor(R.color.rejectFactorText)
            )
        else
            binding.textViewBalance.setTextColor(
                binding.textViewBalance.context.getColor(R.color.confirmFactorText)
            )
        if (item.credit > 0)
            binding.textViewCreditor.setTextColor(
                binding.textViewCreditor.context.getColor(R.color.confirmFactorText)
            )
        if (item.debit > 0)
            binding.textViewDebit.setTextColor(
                binding.textViewDebit.context.getColor(R.color.rejectFactorText)
            )
    }
    //---------------------------------------------------------------------------------------------- setValue


}