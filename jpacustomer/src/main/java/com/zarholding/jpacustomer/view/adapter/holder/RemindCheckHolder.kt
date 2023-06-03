package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.customer.NotDueCheckModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemRemindCheckBinding
import com.zarholding.jpacustomer.view.adapter.recycler.RemindCheckAdapter


/**
 * create by m-latifi on 5/8/2023
 */

class RemindCheckHolder(
    private val binding: ItemRemindCheckBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {


    interface Click{

        fun click(position: Int, item: NotDueCheckModel)

    }

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: NotDueCheckModel, position: Int) {
        setValueToXml(item)
        setListener(item, position)
        setItemSelected(position)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind



    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: NotDueCheckModel) {
        binding.textViewTitle.text = item.checkDate
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: NotDueCheckModel, position: Int) {
        binding.root.setOnClickListener {
            click.click(position, item)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setItemSelected
    private fun setItemSelected(position: Int) {
        if (RemindCheckAdapter.selectedPosition == position){
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.primaryColor))
            binding.textViewTitle
                .setTextColor(binding.textViewTitle.context.getColor(R.color.cardViewMenuColor))
        } else {
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.cardViewMenuColor))
            binding.textViewTitle
                .setTextColor(binding.textViewTitle.context.getColor(R.color.primaryColor))
        }
    }
    //---------------------------------------------------------------------------------------------- setItemSelected

}