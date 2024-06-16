package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.enums.EnumState
import com.hoomanholding.applibrary.model.data.response.basket.subuser.SubUserOrderModel
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderModel
import com.zarholding.jpacustomer.databinding.ItemSubCustomerOrderBinding
import com.zarholding.jpacustomer.view.adapter.holder.SubCustomerOrderHolder


class SubCustomerOrderAdapter(
    private val items: List<SubUserOrderModel>,
    private val onClick: (state: EnumState, item: SubUserOrderModel, position: Int) -> Unit
): RecyclerView.Adapter<SubCustomerOrderHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCustomerOrderHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return SubCustomerOrderHolder(
            ItemSubCustomerOrderBinding.inflate(inflater!!, parent, false),
            onClick
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: SubCustomerOrderHolder, position: Int) {
        holder.bind(item = items[position], position = position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount
}