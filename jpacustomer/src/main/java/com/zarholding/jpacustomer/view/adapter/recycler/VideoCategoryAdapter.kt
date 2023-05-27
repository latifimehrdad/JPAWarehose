package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zarholding.jpacustomer.databinding.ItemVideoCategoryBinding
import com.zarholding.jpacustomer.view.adapter.holder.VideoCategoryHolder

/**
 * Created by m-latifi on 5/27/2023.
 */

class VideoCategoryAdapter(
    private val items: List<String>,
    private val click: VideoCategoryHolder.Click
): RecyclerView.Adapter<VideoCategoryHolder>() {

    companion object{
        var selectedPosition: Int = 0
    }

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoCategoryHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return VideoCategoryHolder(
            ItemVideoCategoryBinding.inflate(inflater!!, parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: VideoCategoryHolder, position: Int) {
        holder.bind(items[position], position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount(): Int  = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}