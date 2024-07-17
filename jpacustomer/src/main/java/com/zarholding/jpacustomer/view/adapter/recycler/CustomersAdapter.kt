package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.customer.CustomersModel
import com.zarholding.jpacustomer.databinding.ItemCustomersBinding
import com.zarholding.jpacustomer.view.adapter.holder.CustomersHolder


class CustomersAdapter(
    private val items: List<CustomersModel>,
    private val onClick: (CustomersModel) -> Unit
) : RecyclerView.Adapter<CustomersHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomersHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CustomersHolder(
            binding = ItemCustomersBinding.inflate(inflater!!, parent, false),
            onClick = onClick
        )
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: CustomersHolder, position: Int) {
        holder.bind(item = items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}