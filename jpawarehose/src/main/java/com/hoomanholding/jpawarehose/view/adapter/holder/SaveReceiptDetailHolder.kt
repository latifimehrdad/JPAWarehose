package com.hoomanholding.jpawarehose.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemReceiptDetailBinding
import com.hoomanholding.applibrary.model.data.database.join.ReceiptAmountWhitProductModel

/**
 * Created by zar on 2/20/2023.
 */

class SaveReceiptDetailHolder(
    private val binding: ItemReceiptDetailBinding
): RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: ReceiptAmountWhitProductModel) {
        binding.item = item
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}