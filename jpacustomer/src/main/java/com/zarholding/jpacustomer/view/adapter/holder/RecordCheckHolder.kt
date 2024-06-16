package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.enums.EnumMontazerVosolReason
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

        val state = when(item.montazerVosolReason) {
            EnumMontazerVosolReason.NotRecord -> {
                context.getString(R.string.notRecord)
            }
            EnumMontazerVosolReason.NotPass -> {
                context.getString(R.string.notPass)
            }
        }
        binding.textViewState.setTitleAndValue(
            title = context.getString(R.string.status),
            splitter = context.getString(R.string.colon),
            value = state
        )
        if (item.montazerVosolReason == EnumMontazerVosolReason.NotPass) {
            binding.cardViewParent.setCardBackgroundColor(context.getColor(R.color.red))
            binding.textViewState.setTextColor(context.getColor(R.color.red))
        }
    }

}