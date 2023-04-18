package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailModel
import com.hoomanholding.jpamanager.databinding.ItemCustomerHyperlinkBinding
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerHyperLinkHolder


/**
 * create by m-latifi on 4/18/2023
 */

class CustomerHyperLinkAdapter(
    private val items: List<CustomerFinancialDetailModel>
): RecyclerView.Adapter<CustomerHyperLinkHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerHyperLinkHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CustomerHyperLinkHolder(
            ItemCustomerHyperlinkBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: CustomerHyperLinkHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount



    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount()= items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}