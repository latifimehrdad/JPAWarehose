package com.hoomanholding.jpawarehose.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemHistorySaveReceiptBinding
import com.hoomanholding.jpawarehose.model.data.database.join.HistorySaveReceiptWithSupplier
import com.hoomanholding.jpawarehose.view.adapter.holder.HistorySaveReceiptHolder

/**
 * Created by zar on 2/20/2023.
 */

class HistorySaveReceiptAdapter(
    private val items: List<HistorySaveReceiptWithSupplier>
):
RecyclerView.Adapter<HistorySaveReceiptHolder>(){

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorySaveReceiptHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return HistorySaveReceiptHolder(ItemHistorySaveReceiptBinding.inflate(
            inflater!!,parent, false
        ))
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder



    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: HistorySaveReceiptHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount



    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount



}