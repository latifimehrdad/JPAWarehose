package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnReportModel
import com.zarholding.jpacustomer.databinding.ItemReportPdfCustomerBillingReturnBinding
import com.zarholding.jpacustomer.view.adapter.holder.BillingReturnPDFHolder

/**
 * Created by m-latifi on 6/8/2023.
 */

class BillingReturnPDFAdapter(
    private val items: List<BillingAndReturnReportModel>
): RecyclerView.Adapter<BillingReturnPDFHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingReturnPDFHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return BillingReturnPDFHolder(
            ItemReportPdfCustomerBillingReturnBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: BillingReturnPDFHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}