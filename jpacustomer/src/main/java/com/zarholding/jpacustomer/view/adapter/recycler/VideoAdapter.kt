package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zarholding.jpacustomer.databinding.ItemVideoBinding
import com.zarholding.jpacustomer.view.adapter.holder.VideoHolder

/**
 * Created by m-latifi on 5/27/2023.
 */

class VideoAdapter(
    private val items: List<String>,
    private val click: VideoHolder.Click
): RecyclerView.Adapter<VideoHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return VideoHolder(
            ItemVideoBinding.inflate(inflater!!, parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bind(items[position], position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount(): Int  = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}