package com.hoomanholding.jpawarehose.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemHistoryReceiptDetailBinding
import com.hoomanholding.jpawarehose.model.data.database.join.ProductWithHistoryReceipt
import com.hoomanholding.jpawarehose.view.adapter.holder.HistorySaveReceiptDetailHolder

/**
 * Created by zar on 2/20/2023.
 */

class HistorySaveReceiptDetailAdapter(
    private val items: List<ProductWithHistoryReceipt>
) :
    RecyclerView.Adapter<HistorySaveReceiptDetailHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistorySaveReceiptDetailHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return HistorySaveReceiptDetailHolder(
            ItemHistoryReceiptDetailBinding.inflate(
                inflater!!, parent, false
            )
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: HistorySaveReceiptDetailHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount


}