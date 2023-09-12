package com.hoomanholding.jpamanager.view.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.user.UserModel
import com.hoomanholding.jpamanager.databinding.ItemUserBinding
import com.hoomanholding.jpamanager.view.adapter.holder.UserHolder


/**
 * create by m-latifi on 3/18/2023
 */

class UserAdapter(
    private val items: List<UserModel>
): RecyclerView.Adapter<UserHolder>() {

    private var inflater: LayoutInflater? = null

    //---------------------------------------------------------------------------------------------- onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)
        return UserHolder(
            ItemUserBinding.inflate(inflater!!, parent, false)
        )
    }
    //---------------------------------------------------------------------------------------------- onCreateViewHolder



    //---------------------------------------------------------------------------------------------- onBindViewHolder
    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(items[position])
    }
    //---------------------------------------------------------------------------------------------- onBindViewHolder


    //---------------------------------------------------------------------------------------------- getItemCount
    override fun getItemCount() = items.size
    //---------------------------------------------------------------------------------------------- getItemCount

}