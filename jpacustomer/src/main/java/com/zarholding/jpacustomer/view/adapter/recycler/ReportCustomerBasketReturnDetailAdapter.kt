package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerReturnBasketReportItemModel
import com.zarholding.jpacustomer.databinding.ItemReportCustomerBasketReturnDetailBinding
import com.zarholding.jpacustomer.view.adapter.holder.ReportCustomerBasketReturnDetailHolder

class ReportCustomerBasketReturnDetailAdapter(
    private val items: List<CustomerReturnBasketReportItemModel>
): RecyclerView.Adapter<ReportCustomerBasketReturnDetailHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportCustomerBasketReturnDetailHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return ReportCustomerBasketReturnDetailHolder(
            ItemReportCustomerBasketReturnDetailBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: ReportCustomerBasketReturnDetailHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}