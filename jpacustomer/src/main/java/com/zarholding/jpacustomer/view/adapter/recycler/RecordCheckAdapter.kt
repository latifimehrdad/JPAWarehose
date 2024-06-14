package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.check.RecordCheckModel
import com.zarholding.jpacustomer.databinding.ItemRecordCheckBinding
import com.zarholding.jpacustomer.view.adapter.holder.RecordCheckHolder

class RecordCheckAdapter(
    private val items: List<RecordCheckModel>
): RecyclerView.Adapter<RecordCheckHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordCheckHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return RecordCheckHolder(
            ItemRecordCheckBinding.inflate(inflater!!,parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: RecordCheckHolder, position: Int) {
        holder.binding(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}