package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorModel
import com.hoomanholding.jpamanager.databinding.ItemVisitorBinding

/**
 * Created by m-latifi on 11/17/2022.
 */

class VisitorSelectHolder(
    private val binding: ItemVisitorBinding,
    private val click: Click
) : RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- Click
    interface Click {
        fun select(item : VisitorModel)
    }
    //---------------------------------------------------------------------------------------------- Click


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item : VisitorModel) {
        binding.item = item
        binding.root.setOnClickListener {
            click.select(item)
        }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}