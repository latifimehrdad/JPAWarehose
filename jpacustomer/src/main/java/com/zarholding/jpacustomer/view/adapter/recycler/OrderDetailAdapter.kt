package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderDetailModel
import com.zarholding.jpacustomer.databinding.ItemOrderDetailBinding
import com.zarholding.jpacustomer.view.adapter.holder.OrderDetailHolder


/**
 * create by m-latifi on 5/8/2023
 */

class OrderDetailAdapter(
    private val items: List<CustomerOrderDetailModel>
): RecyclerView.Adapter<OrderDetailHolder>() {

    companion object{
        var selectedPosition: Int = 0
    }

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return OrderDetailHolder(
            ItemOrderDetailBinding.inflate(inflater!!,parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: OrderDetailHolder, position: Int) {
        holder.bind(items[position], position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}