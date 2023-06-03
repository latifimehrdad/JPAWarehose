package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.customer.NotDueCheckModel
import com.zarholding.jpacustomer.databinding.ItemRemindCheckBinding
import com.zarholding.jpacustomer.view.adapter.holder.RemindCheckHolder


/**
 * create by m-latifi on 5/8/2023
 */

class RemindCheckAdapter(
    private val items: List<NotDueCheckModel>,
    private val click: RemindCheckHolder.Click
): RecyclerView.Adapter<RemindCheckHolder>() {

    companion object{
        var selectedPosition: Int = 0
    }

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindCheckHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return RemindCheckHolder(
            ItemRemindCheckBinding.inflate(inflater!!,parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: RemindCheckHolder, position: Int) {
        holder.bind(items[position], position)
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}