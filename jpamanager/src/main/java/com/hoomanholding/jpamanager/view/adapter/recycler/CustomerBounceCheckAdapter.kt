package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerBounceCheckReportModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerBounceCheckBinding
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerBounceCheckHolder


/**
 * create by m-latifi on 5/1/2023
 */

class CustomerBounceCheckAdapter(
    private val items: List<CustomerBounceCheckReportModel>
): RecyclerView.Adapter<CustomerBounceCheckHolder>() {

    var inflater : LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerBounceCheckHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CustomerBounceCheckHolder(
            ItemCustomerBounceCheckBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: CustomerBounceCheckHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}