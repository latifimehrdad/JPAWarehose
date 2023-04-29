package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerBalanceReportBinding
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerBalanceHolder


/**
 * create by m-latifi on 4/29/2023
 */

class CustomerBalanceAdapter(
    private val items: List<CustomerBalanceReportModel>,
    private val click: CustomerBalanceHolder.Click
): RecyclerView.Adapter<CustomerBalanceHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerBalanceHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CustomerBalanceHolder(
            ItemCustomerBalanceReportBinding.inflate(inflater!!, parent, false),
            click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: CustomerBalanceHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}