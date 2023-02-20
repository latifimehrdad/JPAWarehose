package com.hoomanholding.jpawarehose.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemHistoryReceiptDetailBinding
import com.hoomanholding.jpawarehose.model.data.database.join.ProductWithHistoryReceipt

/**
 * Created by zar on 2/20/2023.
 */

class HistorySaveReceiptDetailHolder(
    private val binding: ItemHistoryReceiptDetailBinding
): RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: ProductWithHistoryReceipt) {
        binding.item = item
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}