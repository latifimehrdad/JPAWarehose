package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderModel
import com.zarholding.jpacustomer.databinding.ItemReportCustomerOrderBinding
import com.zarholding.jpacustomer.view.adapter.holder.ReportCustomerOrderHolder

/**
 * Created by m-latifi on 6/8/2023.
 */

class ReportCustomerOrderAdapter(
    private val items: List<ReportCustomerOrderModel>
): RecyclerView.Adapter<ReportCustomerOrderHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportCustomerOrderHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return ReportCustomerOrderHolder(
            ItemReportCustomerOrderBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: ReportCustomerOrderHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}