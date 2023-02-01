package com.hoomanholding.jpawarehose.view.adapter.holder

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.ItemProductSaveReceiptBinding
import com.hoomanholding.jpawarehose.model.database.join.ProductAmountModel
import com.hoomanholding.jpawarehose.utility.CreateDrawableByBrand
import com.zar.core.tools.manager.DialogManager


/**
 * Create by Mehrdad on 1/27/2023
 */
class ProductSaveReceiptHolder(
    private val binding: ItemProductSaveReceiptBinding,
    private val click: Click
) :
    RecyclerView.ViewHolder(binding.root) {


    interface Click {
        fun addCarton(position: Int)
        fun addPacket(position: Int)
        fun minusCarton(position: Int)
        fun minusPacket(position: Int)
        fun setCarton(position: Int, amount: Int)
        fun setPacket(position: Int, amount: Int)
    }

    //---------------------------------------------------------------------------------------------- bing
    fun bing(product: ProductAmountModel, position: Int) {
        binding.item = product

        setListener(position)

        val cartonCount = product.saveReceiptAmountEntity?.cartonCount ?: 0
        val packetCount = product.saveReceiptAmountEntity?.packetCount ?: 0

        if (cartonCount == 0 && packetCount == 0) {
            binding.materialButtonAdd.visibility = View.VISIBLE
            binding.textViewTotal.visibility = View.GONE
            binding.constraintLayoutCarton.visibility = View.GONE
            binding.constraintLayoutPacket.visibility = View.GONE
        } else {
            binding.materialButtonAdd.visibility = View.GONE
            binding.textViewTotal.visibility = View.VISIBLE
            binding.constraintLayoutCarton.visibility = View.VISIBLE
            binding.constraintLayoutPacket.visibility = View.VISIBLE
        }

        val context = binding.root.context
        val brand = product.brandEntity
        val color = brand?.let {
            Color.rgb(it.r, it.g, it.b)
        } ?: run {
            Color.rgb(200, 200, 200)
        }

        binding.constraintLayoutContent.background =
            CreateDrawableByBrand().getCornerRadius(color)
        binding.textViewTotal.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewTotal.setTextColor(color)

        setCartonColor(color)
        setPacketColor(color)
        binding.materialButtonAdd.backgroundTintList = ColorStateList.valueOf(color)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bing


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(position: Int) {
        binding.materialButtonAdd.setOnClickListener {
            binding.materialButtonAdd.visibility = View.GONE
            binding.textViewTotal.visibility = View.VISIBLE
            binding.constraintLayoutCarton.visibility = View.VISIBLE
            binding.constraintLayoutPacket.visibility = View.VISIBLE
        }

        binding.textViewCartonAdd.setOnClickListener {
            click.addCarton(position)
        }

        binding.textViewCartonMinus.setOnClickListener {
            click.minusCarton(position)
        }

        binding.textViewPacketAdd.setOnClickListener {
            click.addPacket(position)
        }

        binding.textViewPacketMinus.setOnClickListener {
            click.minusPacket(position)
        }

        binding.textViewCartonCount.setOnClickListener {
            showDialogConfirm(binding.root.context, position, true)
        }

        binding.textViewPacketCount.setOnClickListener {
            showDialogConfirm(binding.root.context, position, false)
        }


    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setCartonColor
    private fun setCartonColor(color: Int) {
        val context = binding.root.context
        binding.constraintLayoutCarton.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewCartonAdd.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewCartonAdd.setTextColor(color)
        binding.textViewCartonMinus.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewCartonMinus.setTextColor(color)
    }
    //---------------------------------------------------------------------------------------------- setCartonColor


    //---------------------------------------------------------------------------------------------- setPacketColor
    private fun setPacketColor(color: Int) {
        val context = binding.root.context
        binding.constraintLayoutPacket.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewPacketAdd.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewPacketAdd.setTextColor(color)
        binding.textViewPacketMinus.background =
            CreateDrawableByBrand().getRounded(context, color)
        binding.textViewPacketMinus.setTextColor(color)
    }
    //---------------------------------------------------------------------------------------------- setPacketColor


    //---------------------------------------------------------------------------------------------- showDialogConfirm
    private fun showDialogConfirm(context: Context, position: Int, carton: Boolean) {
        val dialog = DialogManager().createDialogHeightWrapContent(
            context,
            R.layout.dialog_set_amount,
            Gravity.CENTER,
            0
        )

        val amount = if (carton)
            binding.item?.saveReceiptAmountEntity?.cartonCount
        else
            binding.item?.saveReceiptAmountEntity?.packetCount

        val editText = dialog.findViewById<TextInputEditText>(R.id.textInputEditTextAmount)
        val buttonNo = dialog.findViewById<MaterialButton>(R.id.buttonNo)
        val buttonYes = dialog.findViewById<MaterialButton>(R.id.buttonYes)

        amount?.let {
            if (it > 0)
                editText.setText(amount.toString())
            else
                editText.setText("")
        } ?: run { editText.setText("") }

        buttonNo.setOnClickListener { dialog.dismiss() }

        buttonYes.setOnClickListener {
            if (editText.text.isNullOrEmpty()) {
                editText.error = context.getString(R.string.countEmpty)
                return@setOnClickListener
            }
            if (editText.text.toString().isDigitsOnly()) {
                val count = editText.text.toString().toInt()
                if (carton)
                    click.setCarton(position, count)
                else
                    click.setPacket(position, count)
                dialog.dismiss()
            }
        }

        dialog.show()
    }
    //---------------------------------------------------------------------------------------------- showDialogConfirm


}