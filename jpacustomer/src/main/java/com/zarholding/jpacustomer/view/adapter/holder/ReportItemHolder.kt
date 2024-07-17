package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.zarholding.jpacustomer.databinding.ItemReportBinding
import com.zarholding.jpacustomer.model.ReportItem


class ReportItemHolder(
    private val binding: ItemReportBinding,
    private val onClick: (ReportItem) -> Unit
): RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: ReportItem) {
        binding.textViewTitle.text = item.title
        binding.root.setOnClickListener { onClick(item) }
    }
    //---------------------------------------------------------------------------------------------- bind

}