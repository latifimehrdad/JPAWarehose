package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.zarholding.jpacustomer.databinding.ItemVideoBinding

/**
 * Created by m-latifi on 5/27/2023.
 */

class VideoHolder(
    private val binding: ItemVideoBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {

    interface Click{
        fun click(position: Int)
    }


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: String, position: Int) {
        setValueToXml(item)
        setItemSelected(position)
        setListener(position)
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: String) {
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setItemSelected
    private fun setItemSelected(position: Int) {

    }
    //---------------------------------------------------------------------------------------------- setItemSelected



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(position: Int) {
        binding.root.setOnClickListener {
            click.click(position)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener

}