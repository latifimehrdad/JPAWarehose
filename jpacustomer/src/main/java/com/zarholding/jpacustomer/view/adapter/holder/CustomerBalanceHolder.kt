package com.zarholding.jpacustomer.view.adapter.holder

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setAmount
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemReportCustomerBalanceBinding
import kotlin.math.abs

/**
 * Created by m-latifi on 6/7/2023.
 */

class CustomerBalanceHolder(
    private val binding: ItemReportCustomerBalanceBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerBalanceReportDetailModel) {
        setValueToXml(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: CustomerBalanceReportDetailModel) {
        binding.textViewDescription.text = item.description
        binding.textViewDate.setTitleAndValue(
            title = binding.textViewDate.context.getString(R.string.date),
            splitter = binding.textViewDate.context.getString(R.string.colon),
            value = item.transactionDate
        )
        if (item.credit  == 0L)
            binding.textViewCredit.visibility = View.GONE
        else {
            binding.textViewCredit.setTitleAndValue(
                title = binding.textViewCredit.context.getString(R.string.credit),
                splitter = binding.textViewCredit.context.getString(R.string.colon),
                last = binding.textViewCredit.context.getString(R.string.rial),
                value = item.credit
            )
            binding.textViewCredit.visibility = View.VISIBLE
        }

        if (item.debit == 0L)
            binding.textViewDebit.visibility = View.GONE
        else {
            binding.textViewDebit.setTitleAndValue(
                title = binding.textViewDebit.context.getString(R.string.debit),
                splitter = binding.textViewDebit.context.getString(R.string.colon),
                last = binding.textViewDebit.context.getString(R.string.rial),
                value = item.debit
            )
            binding.textViewDebit.visibility = View.VISIBLE
        }
        binding.textViewTotal.setAmount(
            abs(item.balance),
            last = binding.textViewTotal.context.getString(R.string.rial)
        )
        val color = if (item.balance > 0)
            ResourcesCompat.getColor(
                binding.textViewTotal.context.resources,
                R.color.debit, binding.textViewTotal.context.theme
            )
        else ResourcesCompat.getColor(
            binding.textViewTotal.context.resources,
            R.color.credit, binding.textViewTotal.context.theme
        )
        binding.textViewTotalTitle.text = if (item.balance > 0)
            binding.textViewTotalTitle.context.getString(R.string.totalDebit)
        else
            binding.textViewTotalTitle.context.getString(R.string.totalCredit)
        binding.textViewTotal.setTextColor(color)
        binding.textViewTotalTitle.setTextColor(color)
    }
    //---------------------------------------------------------------------------------------------- setValueToXml
}