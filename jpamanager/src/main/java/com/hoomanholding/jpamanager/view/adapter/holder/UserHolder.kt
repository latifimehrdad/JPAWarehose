package com.hoomanholding.jpamanager.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.user.UserModel
import com.hoomanholding.applibrary.tools.CreateDrawable
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.ItemUserBinding


/**
 * create by m-latifi on 3/18/2023
 */

class UserHolder(
    private val binding: ItemUserBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: UserModel) {
        binding.item = item
        checkSelected(item)
        binding.root.setOnClickListener {
            item.select = !item.select
            checkSelected(item)
        }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- checkSelected
    private fun checkSelected(item: UserModel) {
        when (item.select) {
            false -> binding.constraintLayoutParent.background = CreateDrawable().getCornerRadius(
                binding.constraintLayoutParent.context.getColor(R.color.hint))
            true -> binding.constraintLayoutParent.background = CreateDrawable().getCornerRadius(
                binding.constraintLayoutParent.context.getColor(R.color.confirmFactorText))
        }
    }
    //---------------------------------------------------------------------------------------------- checkSelected

}