package com.zarholding.jpacustomer.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.critic.CriticDetailModel
import com.zarholding.jpacustomer.databinding.ItemCriticDetailBinding
import com.zarholding.jpacustomer.databinding.ItemCriticDetailMeBinding
import com.zarholding.jpacustomer.view.adapter.holder.CriticDetailHolder
import com.zarholding.jpacustomer.view.adapter.holder.CriticDetailMeHolder
import com.zarholding.jpacustomer.view.adapter.holder.CriticDetailSupportHolder

/**
 * Created by m-latifi on 8/2/2023.
 */

class CriticDetailAdapter(
    private val items: List<CriticDetailModel>
) : RecyclerView.Adapter<CriticDetailHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriticDetailHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            0 -> CriticDetailMeHolder(ItemCriticDetailMeBinding.inflate(inflater!!, parent, false))
            else -> CriticDetailSupportHolder(ItemCriticDetailBinding.inflate(inflater!!, parent, false))
        }
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun onBindViewHolder(holder: CriticDetailHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount


    //---------------------------------------------------------------------------------------------- getItemViewType
    override fun getItemViewType(position: Int) =
        if (items[position].isAdmin)
            1
        else
            0
    //---------------------------------------------------------------------------------------------- getItemViewType
}