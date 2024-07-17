package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.customer.CustomersModel
import com.zarholding.jpacustomer.databinding.ItemCustomersBinding


class CustomersHolder(
    private val binding: ItemCustomersBinding,
    private val onClick: (CustomersModel) -> Unit
): RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomersModel) {
        binding.textViewName.text = item.customerName
        binding.root.setOnClickListener { onClick(item) }

    }
    //---------------------------------------------------------------------------------------------- bind

}