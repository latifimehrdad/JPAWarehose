package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemCardBoardBinding
import com.hoomanholding.jpamanager.model.data.other.CardBoardItemModel


/**
 * create by m-latifi on 3/6/2023
 */

class CardBoardItemHolder(
    private val binding: ItemCardBoardBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- Click
    interface Click {
        fun itemClick(action: Int)
    }
    //---------------------------------------------------------------------------------------------- Click



    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CardBoardItemModel) {
        binding.item = item
        binding.root.setOnClickListener { click.itemClick(item.link) }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind

}