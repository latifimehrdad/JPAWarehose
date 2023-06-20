package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderDetailItemModel
import com.zarholding.jpacustomer.databinding.ItemReportPdfBillReturnDetailBinding
import com.zarholding.jpacustomer.view.adapter.holder.BillingReturnDetailPDFHolder

/**
 * Created by m-latifi on 6/8/2023.
 */

class BillingReturnDetailPDFAdapter(
    private val items: List<CustomerOrderDetailItemModel>
): RecyclerView.Adapter<BillingReturnDetailPDFHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingReturnDetailPDFHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return BillingReturnDetailPDFHolder(
            ItemReportPdfBillReturnDetailBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: BillingReturnDetailPDFHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}