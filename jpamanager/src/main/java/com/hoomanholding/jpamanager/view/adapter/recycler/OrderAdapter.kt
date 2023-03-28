package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemOrderBinding
import com.hoomanholding.jpamanager.model.data.response.order.OrderModel
import com.hoomanholding.jpamanager.view.adapter.holder.OrderHolder


/**
 * create by m-latifi on 3/18/2023
 */

class OrderAdapter(
    private val items: List<OrderModel>,
    private val click: OrderHolder.Click
): RecyclerView.Adapter<OrderHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return OrderHolder(
            ItemOrderBinding.inflate(inflater!!, parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder



    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}