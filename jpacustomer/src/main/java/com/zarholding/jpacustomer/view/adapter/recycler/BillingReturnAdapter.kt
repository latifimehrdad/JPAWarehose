package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnReportModel
import com.zarholding.jpacustomer.databinding.ItemReportCustomerBillingReturnBinding
import com.zarholding.jpacustomer.view.adapter.holder.BillingReturnHolder

/**
 * Created by m-latifi on 6/8/2023.
 */

class BillingReturnAdapter(
    private val items: List<BillingAndReturnReportModel>
): RecyclerView.Adapter<BillingReturnHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingReturnHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return BillingReturnHolder(
            ItemReportCustomerBillingReturnBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: BillingReturnHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}