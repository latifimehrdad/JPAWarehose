package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerBinding
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerSelectHolder

/**
 * Created by m-latifi on 11/22/2022.
 */

class CustomerSelectAdapter(
    private val items: MutableList<CustomerModel>,
    private val click: CustomerSelectHolder.Click
) : RecyclerView.Adapter<CustomerSelectHolder>() {

    private var layoutInflater: LayoutInflater? = null


    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerSelectHolder {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.context)
        return CustomerSelectHolder(
            ItemCustomerBinding.inflate(layoutInflater!!, parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: CustomerSelectHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- onBindViewHolder


}