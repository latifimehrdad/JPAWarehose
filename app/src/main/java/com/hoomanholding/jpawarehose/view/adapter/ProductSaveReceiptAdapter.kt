package com.hoomanholding.jpawarehose.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemProductSaveReceiptBinding
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.database.join.ProductWithBrandModel
import com.hoomanholding.jpawarehose.view.adapter.holder.ProductSaveReceiptHolder

/**
 * Create by Mehrdad on 1/27/2023
 */
class ProductSaveReceiptAdapter(
    private val products : List<ProductWithBrandModel>
) : RecyclerView.Adapter<ProductSaveReceiptHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ProductSaveReceiptHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(viewGroup.context)
        return ProductSaveReceiptHolder(ItemProductSaveReceiptBinding.inflate(
            inflater!!,viewGroup,false
        ))
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: ProductSaveReceiptHolder, position: Int) {
        holder.bing(products[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = products.size
    //---------------------------------------------------------------------------------------------- getItemCount
}