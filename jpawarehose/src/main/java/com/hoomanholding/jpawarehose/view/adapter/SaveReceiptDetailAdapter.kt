package com.hoomanholding.jpawarehose.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpawarehose.databinding.ItemReceiptDetailBinding
import com.hoomanholding.applibrary.model.data.database.join.ReceiptAmountWhitProductModel
import com.hoomanholding.jpawarehose.view.adapter.holder.SaveReceiptDetailHolder

/**
 * Created by m-latifi on 2/20/2023.
 */

class SaveReceiptDetailAdapter(
    private val items: List<ReceiptAmountWhitProductModel>
) :
    RecyclerView.Adapter<SaveReceiptDetailHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaveReceiptDetailHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return SaveReceiptDetailHolder(
            ItemReceiptDetailBinding.inflate(
                inflater!!, parent, false
            )
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: SaveReceiptDetailHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount


}