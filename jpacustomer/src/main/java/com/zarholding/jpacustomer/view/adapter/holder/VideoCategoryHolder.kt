package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.video.VideoCategoryModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemVideoCategoryBinding
import com.zarholding.jpacustomer.view.adapter.recycler.VideoCategoryAdapter

/**
 * Created by m-latifi on 5/27/2023.
 */

class VideoCategoryHolder(
    private val binding: ItemVideoCategoryBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {

    interface Click{
        fun click(item: VideoCategoryModel, position: Int)
    }


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: VideoCategoryModel, position: Int) {
        setValueToXml(item)
        setItemSelected(position)
        setListener(item, position)
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: VideoCategoryModel) {
        binding.textViewTitle.text = item.videoGroupTitle
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setItemSelected
    private fun setItemSelected(position: Int) {
        if (VideoCategoryAdapter.selectedPosition == position){
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.primaryColor))
            binding.textViewTitle
                .setTextColor(binding.textViewTitle.context.getColor(R.color.a_textButton))
        } else {
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.a_textButton))
            binding.textViewTitle
                .setTextColor(binding.textViewTitle.context.getColor(R.color.primaryColor))
        }
    }
    //---------------------------------------------------------------------------------------------- setItemSelected



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: VideoCategoryModel, position: Int) {
        binding.root.setOnClickListener {
            click.click(item, position)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener

}