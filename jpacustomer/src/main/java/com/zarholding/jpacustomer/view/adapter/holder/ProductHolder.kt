package com.zarholding.jpacustomer.view.adapter.holder

import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.downloadImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemProductBinding

/**
 * Created by m-latifi on 5/14/2023.
 */

class ProductHolder(
    private val binding: ItemProductBinding,
    private val click: Click
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- Click
    interface Click {
        fun selectProduct(item: ProductModel, imageView: ImageView)
    }
    //---------------------------------------------------------------------------------------------- Click


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: ProductModel) {
        setValueToXml(item)
        setListener(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: ProductModel) {
        binding.textViewName.text = item.productName
        binding.textViewPrice.text = item.price.split()
        binding.textViewCode.setTitleAndValue(
            title = binding.textViewCode.context.getString(R.string.productCode),
            splitter = binding.textViewCode.context.getString(R.string.colon),
            value = item.productCode
        )
        binding.imageViewPicture.downloadImage(
            url = item.productThumbnailImageName,
            placeholder = AppCompatResources
                .getDrawable(binding.imageViewPicture.context, R.drawable.a_ic_logo)
        )
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: ProductModel) {
        binding.root.setOnClickListener {
            click.selectProduct(item, binding.imageViewPicture)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener
}