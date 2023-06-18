package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.zarholding.jpacustomer.databinding.ItemReportPdfCustomerBalanceBinding
import com.zarholding.jpacustomer.view.adapter.holder.BalancePDFHolder

/**
 * Created by m-latifi on 6/8/2023.
 */

class BalancePDFAdapter(
    private val items: List<CustomerBalanceReportDetailModel>
): RecyclerView.Adapter<BalancePDFHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalancePDFHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return BalancePDFHolder(
            ItemReportPdfCustomerBalanceBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: BalancePDFHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}