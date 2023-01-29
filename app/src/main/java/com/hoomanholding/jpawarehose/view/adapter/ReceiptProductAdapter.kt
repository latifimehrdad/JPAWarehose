package com.hoomanholding.jpawarehose.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemReceiptProductBinding
import com.hoomanholding.jpawarehose.model.database.join.ReceiptWithProduct
import com.hoomanholding.jpawarehose.view.adapter.holder.ReceiptProductHolder

/**
 * Create by Mehrdad on 1/27/2023
 */
class ReceiptProductAdapter(
    private val products : List<ReceiptWithProduct>
) : RecyclerView.Adapter<ReceiptProductHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ReceiptProductHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(viewGroup.context)
        return ReceiptProductHolder(ItemReceiptProductBinding.inflate(
            inflater!!,viewGroup,false
        ))
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: ReceiptProductHolder, position: Int) {
        holder.bing(products[position], position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = products.size
    //---------------------------------------------------------------------------------------------- getItemCount
}