package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.zarholding.jpacustomer.databinding.ItemProductBinding
import com.zarholding.jpacustomer.view.adapter.holder.ProductHolder

/**
 * Created by m-latifi on 5/14/2023.
 */

class ProductAdapter(
    private val items: List<ProductModel>,
    private val click: ProductHolder.Click
): RecyclerView.Adapter<ProductHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return ProductHolder(
            ItemProductBinding.inflate(inflater!!, parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(items[position], position)
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- changeCount
    fun changeCount(position: Int, count: Int) {
        items[position].count = count
    }
    //---------------------------------------------------------------------------------------------- changeCount

}