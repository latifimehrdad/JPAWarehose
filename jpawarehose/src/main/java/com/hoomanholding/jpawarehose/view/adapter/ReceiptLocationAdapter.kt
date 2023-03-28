package com.hoomanholding.jpawarehose.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemReceiptLocationBinding
import com.hoomanholding.applibrary.model.data.database.entity.BrandEntity
import com.hoomanholding.applibrary.model.data.database.join.LocationWithAmount
import com.hoomanholding.jpawarehose.view.adapter.holder.ReceiptLocationHolder

class ReceiptLocationAdapter(
    private val brand : BrandEntity,
    private val locations : List<LocationWithAmount>,
    private val click: ReceiptLocationHolder.Click
) : RecyclerView.Adapter<ReceiptLocationHolder>(){


    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ReceiptLocationHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(viewGroup.context)
        return ReceiptLocationHolder(ItemReceiptLocationBinding.inflate(
            inflater!!,viewGroup, false
        ), click)
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: ReceiptLocationHolder, position: Int) {
        holder.bind(locations[position], brand, position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = locations.size
    //---------------------------------------------------------------------------------------------- getItemCount
}