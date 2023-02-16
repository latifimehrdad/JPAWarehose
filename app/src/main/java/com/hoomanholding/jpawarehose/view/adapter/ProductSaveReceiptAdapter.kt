package com.hoomanholding.jpawarehose.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemProductSaveReceiptBinding
import com.hoomanholding.jpawarehose.model.data.database.join.ProductAmountModel
import com.hoomanholding.jpawarehose.view.adapter.holder.ProductSaveReceiptHolder

/**
 * Create by Mehrdad on 1/27/2023
 */
class ProductSaveReceiptAdapter(
    private val products : List<ProductAmountModel>,
    private val click: ProductSaveReceiptHolder.Click
) : RecyclerView.Adapter<ProductSaveReceiptHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ProductSaveReceiptHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(viewGroup.context)
        return ProductSaveReceiptHolder(ItemProductSaveReceiptBinding.inflate(
            inflater!!,viewGroup,false
        ), click)
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: ProductSaveReceiptHolder, position: Int) {
        holder.bing(products[position], position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = products.size
    //---------------------------------------------------------------------------------------------- getItemCount
}