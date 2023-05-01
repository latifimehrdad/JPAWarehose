package com.hoomanholding.jpamanager.view.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerHyperlinkBinding


/**
 * create by m-latifi on 4/18/2023
 */

class CustomerHyperLinkHolder(
    private val binding: ItemCustomerHyperlinkBinding
): RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerFinancialDetailModel, checkType: EnumCheckType){
        binding.item = item
        if (checkType == EnumCheckType.OrderDetails)
            binding.linearLayoutOrder.visibility = View.VISIBLE
        else
            binding.linearLayoutOrder.visibility = View.GONE
        setVisibleTextView(binding.textViewReceiveDate, item.checkRecivedDate)
        setVisibleTextView(binding.textViewDueDate, item.checkDueDate)
        setVisibleTextView(binding.textViewStateDate, item.checkStateDate)
        setVisibleTextView(binding.textViewState, item.checkState)
        setVisibleTextView(binding.textViewReason, item.reason)
        setVisibleTextView(binding.textViewBankName, item.checkBankName)
        setVisibleTextView(binding.textViewBranchName, item.checkBranchName)
        setVisibleTextView(binding.textViewAccountNumber, item.checkAccountNumber)
        setVisibleTextView(binding.textViewOwnerName, item.checkOwnerName)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setVisibleTextView
    private fun setVisibleTextView(textView: TextView, value: String?) {
        if (value.isNullOrEmpty())
            textView.visibility = View.GONE
        else
            textView.visibility = View.VISIBLE
    }
    //---------------------------------------------------------------------------------------------- setVisibleTextView

}