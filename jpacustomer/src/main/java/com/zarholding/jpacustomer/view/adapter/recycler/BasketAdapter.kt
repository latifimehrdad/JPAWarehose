package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.basket.DetailBasketModel
import com.zarholding.jpacustomer.databinding.ItemBasketBinding
import com.zarholding.jpacustomer.view.adapter.holder.BasketHolder

/**
 * Created by m-latifi on 6/6/2023.
 */

class BasketAdapter(
    private val items: List<DetailBasketModel>,
    private val click: BasketHolder.Click
) : RecyclerView.Adapter<BasketHolder>() {

    private var inflater: LayoutInflater? = null
    var productShowListType = false

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return BasketHolder(
            ItemBasketBinding.inflate(inflater!!, parent, false),
            click,
            productShowListType)
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: BasketHolder, position: Int) {
        holder.bind(items[position], position)
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- changeCount
    fun changeCount(position: Int, count: Int){
        items[position].count = count
    }
    //---------------------------------------------------------------------------------------------- changeCount
}