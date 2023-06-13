package com.zarholding.jpacustomer.view.adapter.holder

import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.basket.DetailBasketModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemBasketListBinding
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog

/**
 * Created by m-latifi on 6/6/2023.
 */

class BasketHolderList(
    private val binding: ItemBasketListBinding,
    private val click: BasketHolderNormal.Click
): BasketHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- setValueToXml
    override fun bind(item: DetailBasketModel, position: Int) {
        setValueToXml(item)
        setListener(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: DetailBasketModel) {
        binding.textViewListName.text = item.productName
        binding.textViewListCode.setTitleAndValue(
            title = binding.textViewListCount.context.getString(R.string.productCode),
            splitter = binding.textViewListCode.context.getString(R.string.colon),
            value = item.productCode
        )
        binding.textViewListCount.setTitleAndValue(
            title = binding.textViewListCount.context.getString(R.string.count),
            splitter = binding.textViewListCount.context.getString(R.string.colon),
            value = item.count
        )
    }
    //---------------------------------------------------------------------------------------------- setValueToXml



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: DetailBasketModel) {
        binding.imageViewDelete.setOnClickListener {
            val click = object : ConfirmDialog.Click{
                override fun clickYes() {
                    click.deleteItem(item)
                }
            }
            ConfirmDialog(
                binding.imageViewDelete.context,
                binding.imageViewDelete.context.getString(R.string.doYouWantToBasketDelete, item.productName),
                click,
                false
            ).show()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener

}