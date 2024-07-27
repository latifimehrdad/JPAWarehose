package com.zarholding.jpacustomer.view.adapter.holder

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.downloadImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.view.custom.JpaButton
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


    var textWatcher: TextWatcher? = null

    //---------------------------------------------------------------------------------------------- Click
    interface Click {
        fun selectProduct(item: ProductModel, imageView: ImageView)
    }
    //---------------------------------------------------------------------------------------------- Click


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: ProductModel, position: Int) {
        setValueToXml(item)
        setListener(item, position)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: ProductModel) {
        binding.editTextCount.removeTextChangedListener(textWatcher)
        binding.editTextCount.setText(item.count.toString())
        binding.imageviewStatus.visibility = View.GONE
        binding.imageviewDiscount.visibility = View.GONE
        binding.imageviewTxt.visibility = View.GONE
        binding.textViewName.text = item.productName
        binding.textViewPrice.text = item.price.split()
        binding.textViewCode.setTitleAndValue(
            title = binding.textViewCode.context.getString(R.string.productCode),
            splitter = binding.textViewCode.context.getString(R.string.colon),
            value = item.productCode
        )
        binding.textViewCountInBox.setTitleAndValue(
            title = binding.textViewCountInBox.context.getString(R.string.countInBox),
            splitter = binding.textViewCountInBox.context.getString(R.string.colon),
            value = item.productCountinBox.split()
        )
        binding.textViewCountInPackage.setTitleAndValue(
            title = binding.textViewCountInPackage.context.getString(R.string.countInPackage),
            splitter = binding.textViewCountInPackage.context.getString(R.string.colon),
            value = item.productCountinPackage.split()
        )
        binding.imageViewPicture.downloadImage(
            url = item.productThumbnailImageName,
            placeholder = AppCompatResources
                .getDrawable(binding.imageViewPicture.context, R.drawable.a_ic_logo)
        )
        if (item.isCash)
            binding.imageviewStatus.visibility = View.VISIBLE

        if (!item.txtToOrderForProduct.isNullOrEmpty())
            binding.imageviewTxt.visibility = View.VISIBLE

        if (item.isSpecial)
            binding.imageviewDiscount.visibility = View.VISIBLE



        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().isDigitsOnly()) {
                    item.count = if (s.isNotEmpty()) s.toString().toInt() else 0
                }
            }

        }

        binding.editTextCount.addTextChangedListener(textWatcher)



    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: ProductModel, position: Int) {
        binding.root.setOnClickListener {
            click.selectProduct(item, binding.imageViewPicture)
        }

        binding.imageViewMinus.setOnClickListener {
            val count = item.count - 1
            if (count > -1) {
                binding.editTextCount.setText((count).toString())
                item.count = count
            }
        }

        binding.imageViewPlus.setOnClickListener {
            val count = item.count + 1
            if (count <= item.maxCount) {
                binding.editTextCount.setText((count).toString())
                item.count = count
            }
        }

/*        Log.d("meri", " log5 ==== code = ${item.productCode} - count = ${item.count} - edit = ${binding.editTextCount.text}")
        binding.editTextCount.addTextChangedListener {
            Log.d("meri", " log2 ==== code = ${item.productCode} - count = ${item.count} - edit = ${binding.editTextCount.text}")
//            val text = it.toString()
//            if (text.isNotEmpty())
//                if (text.isDigitsOnly())
//                    if (item.maxCount < text.toInt())
//                        binding.editTextCount.setText("0")
            item.count = 10
            Log.d("meri", " log3 ==== code = ${item.productCode} - count = ${item.count} - edit = ${binding.editTextCount.text}")
        }*/

    }
    //---------------------------------------------------------------------------------------------- setListener


/*
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
*/

}