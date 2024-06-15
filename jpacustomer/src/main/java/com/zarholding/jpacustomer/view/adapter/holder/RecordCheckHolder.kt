package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.check.RecordCheckModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemRecordCheckBinding

class RecordCheckHolder(
    private val binding: ItemRecordCheckBinding
): RecyclerView.ViewHolder(binding.root) {

    fun binding(item: RecordCheckModel) {
        val context = binding.root.context
        binding.textViewCheckNumber.setTitleAndValue(
            title = context.getString(R.string.checkNumber),
            splitter = context.getString(R.string.colon),
            value = item.checkNumber
        )
        binding.textViewShomarehSayadi.setTitleAndValue(
            title = context.getString(R.string.shomarehSayadi),
            splitter = context.getString(R.string.colon),
            value = item.shomarehSayadi
        )
        binding.textViewCheckDate.setTitleAndValue(
            title = context.getString(R.string.checkDate),
            splitter = context.getString(R.string.colon),
            value = item.checkDate
        )
        binding.textViewAmount.setTitleAndValue(
            title = context.getString(R.string.amount),
            splitter = context.getString(R.string.colon),
            value = item.amount.split()
        )
        binding.textViewDiffDay.setTitleAndValue(
            title = context.getString(R.string.diffDay),
            splitter = context.getString(R.string.colon),
            value = item.diffDay
        )
    }

}