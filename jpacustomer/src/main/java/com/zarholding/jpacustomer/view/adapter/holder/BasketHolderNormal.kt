package com.zarholding.jpacustomer.view.adapter.holder

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import com.hoomanholding.applibrary.ext.downloadImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.basket.DetailBasketModel
import com.hoomanholding.applibrary.view.custom.JpaButton
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemBasketNormalBinding
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog

/**
 * Created by m-latifi on 6/6/2023.
 */

class BasketHolderNormal(
    private val binding: ItemBasketNormalBinding,
    private val click: Click
) : BasketHolder(binding.root) {

    //---------------------------------------------------------------------------------------------- Click
    interface Click {
        fun click(item: DetailBasketModel, button: JpaButton, position: Int, count: Int)
        fun deleteItem(item: DetailBasketModel)
    }
    //---------------------------------------------------------------------------------------------- Click


    //---------------------------------------------------------------------------------------------- setValueToXml
    override fun bind(item: DetailBasketModel, position: Int) {
        setValueToXml(item)
        setListener(item, position)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: DetailBasketModel) {
        binding.textViewName.text = item.productName
        binding.textViewPrice.text = item.price.split()
        binding.editTextCount.setText(item.count.toString())
        binding.textViewCode.setTitleAndValue(
            title = binding.textViewCode.context.getString(R.string.productCode),
            splitter = binding.textViewCode.context.getString(R.string.colon),
            value = item.productCode
        )
        binding.imageViewPicture.downloadImage(
            url = item.productThumbnailImageName,
            placeholder = AppCompatResources
                .getDrawable(binding.imageViewPicture.context, R.drawable.ic_logo)
        )
        binding.editTextCount.addTextChangedListener {
            binding.buttonSaveChange.visibility = View.VISIBLE
        }
        binding.buttonSaveChange.visibility = View.GONE
    }
    //---------------------------------------------------------------------------------------------- setValueToXml



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: DetailBasketModel, position: Int) {
        binding.imageViewMinus.setOnClickListener {
            val count = getEditTextCount() - 1
            if (count >= 0)
                binding.editTextCount.setText((count).toString())
        }

        binding.imageViewPlus.setOnClickListener {
            val count = getEditTextCount() + 1
            if (count <= 999)
                binding.editTextCount.setText((count).toString())
        }

        binding.buttonSaveChange.setOnClickListener {
            val count = getEditTextCount()
            if (count <= 0)
                return@setOnClickListener
            click.click(item, binding.buttonSaveChange, position, count)
        }

        binding.imageViewDelete.setOnClickListener {
            val click = object : ConfirmDialog.Click {
                override fun clickYes() {
                    click.deleteItem(item)
                }
            }
            ConfirmDialog(
                binding.imageViewDelete.context,
                binding.imageViewDelete.context.getString(
                    R.string.doYouWantToBasketDelete,
                    item.productName
                ),
                click,
                false
            ).show()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- getEditTextCount
    private fun getEditTextCount(): Int {
        val text = binding.editTextCount.text.toString()
        return if (text.isEmpty())
            0
        else if (text.isDigitsOnly())
            text.toInt()
        else {
            binding.editTextCount.setText("0")
            0
        }
    }
    //---------------------------------------------------------------------------------------------- getEditTextCount

}