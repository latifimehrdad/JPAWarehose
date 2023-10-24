package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderDetailModel
import com.zarholding.jpacustomer.databinding.ItemReportCustomerOrderDetailBinding
import com.zarholding.jpacustomer.view.adapter.holder.ReportCustomerOrderDetailHolder

/**
 * Created by m-latifi on 6/8/2023.
 */

class ReportCustomerOrderDetailAdapter(
    private val items: List<ReportCustomerOrderDetailModel>
): RecyclerView.Adapter<ReportCustomerOrderDetailHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportCustomerOrderDetailHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return ReportCustomerOrderDetailHolder(
            ItemReportCustomerOrderDetailBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: ReportCustomerOrderDetailHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}