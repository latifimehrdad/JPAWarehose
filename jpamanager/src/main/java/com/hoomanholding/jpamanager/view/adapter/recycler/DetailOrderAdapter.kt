package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.order.DetailOrderModel
import com.hoomanholding.jpamanager.databinding.ItemOrderDetailBinding
import com.hoomanholding.jpamanager.view.adapter.holder.DetailOrderHolder


/**
 * create by m-latifi on 4/10/2023
 */

class DetailOrderAdapter(
    private val items: List<DetailOrderModel>
): RecyclerView.Adapter<DetailOrderHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailOrderHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return DetailOrderHolder(
            ItemOrderDetailBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder



    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: DetailOrderHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}