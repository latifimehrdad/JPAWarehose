package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerReturnBasketReportModel
import com.zarholding.jpacustomer.databinding.ItemReportCustomerBasketReturnBinding
import com.zarholding.jpacustomer.view.adapter.holder.ReportCustomerBasketReturnHolder

class ReportCustomerBasketReturnAdapter(
    private val items: List<CustomerReturnBasketReportModel>
): RecyclerView.Adapter<ReportCustomerBasketReturnHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportCustomerBasketReturnHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return ReportCustomerBasketReturnHolder(
            ItemReportCustomerBasketReturnBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: ReportCustomerBasketReturnHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}