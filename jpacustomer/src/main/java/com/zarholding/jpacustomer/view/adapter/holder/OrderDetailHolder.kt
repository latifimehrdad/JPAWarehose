package com.zarholding.jpacustomer.view.adapter.holder

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.downloadImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderDetailModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemOrderDetailBinding

/**
 * Created by m-latifi on 6/21/2023.
 */

class OrderDetailHolder(
    private val binding: ItemOrderDetailBinding
): RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerOrderDetailModel, position: Int) {
        setValueToXml(item, position)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind



    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: CustomerOrderDetailModel, position: Int) {
        binding.textViewPosition.text = (position + 1).toString()
        binding.textViewName.text = item.productName
        binding.textViewCode.setTitleAndValue(
            title = binding.textViewCode.context.getString(R.string.productCode),
            splitter = binding.textViewCode.context.getString(R.string.colon),
            value = item.productCode
        )
        binding.textViewPrice.setTitleAndValue(
            title = binding.textViewPrice.context.getString(R.string.space),
            splitter = binding.textViewPrice.context.getString(R.string.space),
            value = item.price,
            last = binding.textViewPrice.context.getString(R.string.rial)
        )
        binding.textViewCount.setTitleAndValue(
            title = binding.textViewCount.context.getString(R.string.count),
            splitter = binding.textViewCount.context.getString(R.string.colon),
            value = item.count,
            last = binding.textViewCount.context.getString(R.string.pieces)
        )
        binding.textViewAmount.setTitleAndValue(
            title = binding.textViewAmount.context.getString(R.string.totalAmount),
            splitter = binding.textViewAmount.context.getString(R.string.colon),
            value = item.productAmount,
            last = binding.textViewAmount.context.getString(R.string.rial)
        )
        binding.imageViewPicture.downloadImage(
            url = item.productThumbnailImageName,
            placeholder = AppCompatResources
                .getDrawable(binding.imageViewPicture.context, R.drawable.a_ic_logo)
        )
    }
    //---------------------------------------------------------------------------------------------- setValueToXml
}