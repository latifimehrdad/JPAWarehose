package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.critic.CriticModel
import com.zarholding.jpacustomer.databinding.ItemCriticBinding
import com.zarholding.jpacustomer.view.adapter.holder.CriticHolder

/**
 * Created by m-latifi on 8/2/2023.
 */

class CriticAdapter(
    private val items: List<CriticModel>,
    private val onClick: (Long, String?) -> Unit
) : RecyclerView.Adapter<CriticHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriticHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return CriticHolder(
            binding = ItemCriticBinding.inflate(inflater!!, parent, false),
            onClick = onClick
        )
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: CriticHolder, position: Int) {
        holder.bind(item = items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}