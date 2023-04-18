package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemCustomerFinancialBinding
import com.hoomanholding.jpamanager.model.data.other.CustomerFinancialItemModel
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerFinancialDetailHolder


/**
 * create by m-latifi on 4/18/2023
 */

class CustomerFinancialAdapter(
    private val items: List<CustomerFinancialItemModel>,
    private val click: CustomerFinancialDetailHolder.Click
) : RecyclerView.Adapter<CustomerFinancialDetailHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CustomerFinancialDetailHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CustomerFinancialDetailHolder(
            ItemCustomerFinancialBinding.inflate(inflater!!, parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: CustomerFinancialDetailHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}