package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.zarholding.jpacustomer.databinding.ItemProductBinding

/**
 * Created by zar on 5/14/2023.
 */

class ProductHolder(
    private val binding: ItemProductBinding
): RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: ProductModel){
        binding.item = item
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}