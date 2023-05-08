package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.zarholding.jpacustomer.databinding.ItemOrderBinding
import com.zarholding.jpacustomer.view.adapter.holder.OrderHolder


/**
 * create by m-latifi on 5/8/2023
 */

class OrderAdapter(
    private val items: List<CustomerOrderModel>,
    private val click: OrderHolder.Click
): RecyclerView.Adapter<OrderHolder>() {

    companion object{
        var selectedPosition: Int = 0
    }

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return OrderHolder(
            ItemOrderBinding.inflate(inflater!!,parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.bind(items[position], position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}