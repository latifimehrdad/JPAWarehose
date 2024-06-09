package com.zarholding.jpacustomer.view.adapter.holder

import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.downloadImage
import com.hoomanholding.applibrary.model.data.response.category.CategoryModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemCategoryBinding
import com.zarholding.jpacustomer.view.adapter.recycler.CategoryAdapter
import com.zarholding.jpacustomer.view.adapter.recycler.OrderAdapter


/**
 * create by m-latifi on 5/8/2023
 */

class CategoryHolder(
    private val binding: ItemCategoryBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {


    interface Click{
        fun click(position: Int, item: CategoryModel)
    }

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CategoryModel, position: Int) {
        setValueToXml(item)
        setListener(item, position)
        setItemSelected(position)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind



    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: CategoryModel) {
        binding.textViewTitle.text = item.productCategoryName
        binding.imageViewIcon.downloadImage(
                url = item.productCategoryImageName,
                placeholder = AppCompatResources
                        .getDrawable(binding.imageViewIcon.context, R.drawable.a_ic_logo)
        )
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: CategoryModel, position: Int) {
        binding.root.setOnClickListener {
            click.click(position, item)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setItemSelected
    private fun setItemSelected(position: Int) {
        if (CategoryAdapter.selectedPosition == position){
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.primaryColor))
            binding.imageViewIcon.setColorFilter(
                ContextCompat.getColor(binding.imageViewIcon.context, R.color.buttonTextColor),
                android.graphics.PorterDuff.Mode.SRC_IN)
            val color = binding.textViewTitle.context.getColor(R.color.buttonTextColor)
            binding.textViewTitle.setTextColor(color)
        } else {
            binding.cardViewParent
                .setCardBackgroundColor(binding.cardViewParent.context.getColor(R.color.color4))
            binding.imageViewIcon.setColorFilter(
                ContextCompat.getColor(binding.imageViewIcon.context, R.color.textColor),
                android.graphics.PorterDuff.Mode.SRC_IN)
            val color = binding.textViewTitle.context.getColor(R.color.textColor)
            binding.textViewTitle.setTextColor(color)
        }
    }
    //---------------------------------------------------------------------------------------------- setItemSelected

}