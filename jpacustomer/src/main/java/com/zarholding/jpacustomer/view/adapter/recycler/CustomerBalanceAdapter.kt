package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.zarholding.jpacustomer.databinding.ItemReportCustomerBalanceBinding
import com.zarholding.jpacustomer.view.adapter.holder.CustomerBalanceHolder

/**
 * Created by m-latifi on 6/7/2023.
 */

class CustomerBalanceAdapter(
    private val items: List<CustomerBalanceReportDetailModel>
): RecyclerView.Adapter<CustomerBalanceHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerBalanceHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CustomerBalanceHolder(
            ItemReportCustomerBalanceBinding.inflate(inflater!!, parent, false)
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