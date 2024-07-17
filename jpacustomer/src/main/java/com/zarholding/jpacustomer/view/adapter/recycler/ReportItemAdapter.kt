package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zarholding.jpacustomer.databinding.ItemReportBinding
import com.zarholding.jpacustomer.model.ReportItem
import com.zarholding.jpacustomer.view.adapter.holder.ReportItemHolder


class ReportItemAdapter(
    private val items: List<ReportItem>,
    private val onClick: (ReportItem) -> Unit
) : RecyclerView.Adapter<ReportItemHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportItemHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return ReportItemHolder(
            binding = ItemReportBinding.inflate(inflater!!, parent, false),
            onClick = onClick
        )
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: ReportItemHolder, position: Int) {
        holder.bind(item = items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}