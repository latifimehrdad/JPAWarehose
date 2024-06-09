package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.category.CategoryModel
import com.zarholding.jpacustomer.databinding.ItemCategoryBinding
import com.zarholding.jpacustomer.view.adapter.holder.CategoryHolder


/**
 * create by m-latifi on 5/8/2023
 */

class CategoryAdapter(
    private val items: List<CategoryModel>,
    private val click: CategoryHolder.Click
): RecyclerView.Adapter<CategoryHolder>() {

    companion object{
        var selectedPosition: Int = -1
    }

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CategoryHolder(
            ItemCategoryBinding.inflate(inflater!!,parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(items[position], position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}