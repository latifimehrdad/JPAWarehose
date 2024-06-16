package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.basket.subuser.SubUserOrderDetailModel
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderDetailModel
import com.zarholding.jpacustomer.databinding.ItemSubCustomerOrderDetailBinding
import com.zarholding.jpacustomer.view.adapter.holder.SubCustomerOrderDetailHolder


class SubCustomerOrderDetailAdapter(
    private val items: List<SubUserOrderDetailModel>
): RecyclerView.Adapter<SubCustomerOrderDetailHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCustomerOrderDetailHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return SubCustomerOrderDetailHolder(
            ItemSubCustomerOrderDetailBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: SubCustomerOrderDetailHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}