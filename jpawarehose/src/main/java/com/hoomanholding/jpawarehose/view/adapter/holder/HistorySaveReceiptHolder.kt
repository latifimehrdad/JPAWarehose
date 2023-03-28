package com.hoomanholding.jpawarehose.view.adapter.holder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemHistorySaveReceiptBinding
import com.hoomanholding.applibrary.model.data.database.join.HistorySaveReceiptWithSupplier
import com.hoomanholding.jpawarehose.view.adapter.HistorySaveReceiptDetailAdapter

/**
 * Created by zar on 2/20/2023.
 */

class HistorySaveReceiptHolder(
    private val binding: ItemHistorySaveReceiptBinding
): RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: HistorySaveReceiptWithSupplier) {
        binding.item = item
        val adapter = HistorySaveReceiptDetailAdapter(item.amounts)
        val manager = LinearLayoutManager(
            binding.root.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewDetail.layoutManager = manager
        binding.recyclerViewDetail.adapter = adapter
        binding.root.setOnClickListener {
            if (binding.expandableMore.isExpanded)
                binding.expandableMore.collapse()
            else
                binding.expandableMore.expand()
        }

        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}