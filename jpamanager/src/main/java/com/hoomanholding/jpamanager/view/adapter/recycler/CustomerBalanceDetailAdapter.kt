package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerBalanceReportDatailBinding
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerBalanceDetailHolder


/**
 * create by m-latifi on 4/29/2023
 */

class CustomerBalanceDetailAdapter(
    private val items: List<CustomerBalanceReportDetailModel>
): RecyclerView.Adapter<CustomerBalanceDetailHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerBalanceDetailHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CustomerBalanceDetailHolder(
            ItemCustomerBalanceReportDatailBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: CustomerBalanceDetailHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}