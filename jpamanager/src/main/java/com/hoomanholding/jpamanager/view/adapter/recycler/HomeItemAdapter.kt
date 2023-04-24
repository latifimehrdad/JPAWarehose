package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemHomeBinding
import com.hoomanholding.jpamanager.model.data.other.HomeReportItemModel
import com.hoomanholding.jpamanager.view.adapter.holder.HomeItemHolder


/**
 * create by m-latifi on 3/7/2023
 */

class HomeItemAdapter(
    private val items: List<HomeReportItemModel>,
    private val currency: String
): RecyclerView.Adapter<HomeItemHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return HomeItemHolder(
            ItemHomeBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        holder.bind(items[position], currency)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}