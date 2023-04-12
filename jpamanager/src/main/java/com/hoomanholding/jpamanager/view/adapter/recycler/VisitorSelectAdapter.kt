package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorModel
import com.hoomanholding.jpamanager.databinding.ItemVisitorBinding
import com.hoomanholding.jpamanager.view.adapter.holder.VisitorSelectHolder

/**
 * Created by m-latifi on 11/22/2022.
 */

class VisitorSelectAdapter(
    private val items: MutableList<VisitorModel>,
    private val click: VisitorSelectHolder.Click
) : RecyclerView.Adapter<VisitorSelectHolder>() {

    private var layoutInflater: LayoutInflater? = null


    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitorSelectHolder {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.context)
        return VisitorSelectHolder(
            ItemVisitorBinding.inflate(layoutInflater!!, parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: VisitorSelectHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- onBindViewHolder


}