package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemCardBoardBinding
import com.hoomanholding.jpamanager.model.data.other.CardBoardItemModel
import com.hoomanholding.jpamanager.view.adapter.holder.CardBoardItemHolder


/**
 * create by m-latifi on 3/6/2023
 */

class CardBoardItemAdapter(
    private val items: List<CardBoardItemModel>,
    private val click: CardBoardItemHolder.Click
) : RecyclerView.Adapter<CardBoardItemHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardBoardItemHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CardBoardItemHolder(
            ItemCardBoardBinding.inflate(inflater!!, parent, false), click
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: CardBoardItemHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}