package com.hoomanholding.jpawarehose.view.adapter.holder

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.ItemReceiptLocationBinding
import com.hoomanholding.applibrary.model.data.database.entity.BrandEntity
import com.hoomanholding.applibrary.model.data.database.join.LocationWithAmount
import com.hoomanholding.jpawarehose.tools.CreateDrawableByBrand
import com.zar.core.tools.manager.DialogManager

class ReceiptLocationHolder(
    private val binding: ItemReceiptLocationBinding,
    private val click: Click
) : RecyclerView.ViewHolder(binding.root) {

    interface Click {
        fun addToLocation(count: String)
    }

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: LocationWithAmount, brand: BrandEntity, position: Int) {
        binding.item = item
        binding.position = (position + 1).toString()
        val color = Color.rgb(brand.r, brand.g, brand.b)
        binding.color = color

        if (ReceiptProductHolder.locationPosition == position) {
            binding.constraintLayoutParent.alpha = 1.0f
            setOnListener()
        } else
            binding.constraintLayoutParent.alpha = 0.3f


        binding.constraintLayoutLocation.background =
            CreateDrawableByBrand().getRounded(binding.root.context, color)

        binding.imageViewRow.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)

        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setOnListener
    private fun setOnListener() {

        binding.textViewCount.setOnClickListener {
            showDialogConfirm(binding.textViewCount.context)
        }
    }
    //---------------------------------------------------------------------------------------------- setOnListener


    //---------------------------------------------------------------------------------------------- showDialogConfirm
    private fun showDialogConfirm(context: Context) {
        val dialog = DialogManager().createDialogHeightWrapContent(
            context,
            R.layout.dialog_confirm_location_placement,
            Gravity.CENTER,
            0
        )

        val amount = binding.item?.locationAmount?.amount

        val editText = dialog.findViewById<TextInputEditText>(R.id.textInputEditTextNumber)
        val buttonNo = dialog.findViewById<MaterialButton>(R.id.buttonNo)
        val buttonYes = dialog.findViewById<MaterialButton>(R.id.buttonYes)

        amount?.let { editText.setText(amount.toString()) } ?: run { editText.setText("") }

        buttonNo.setOnClickListener { dialog.dismiss() }

        buttonYes.setOnClickListener {
            if (editText.text.isNullOrEmpty()) {
                editText.error = context.getString(R.string.countEmpty)
                return@setOnClickListener
            }
            click.addToLocation(editText.text.toString())
            dialog.dismiss()
        }

        dialog.show()
    }
    //---------------------------------------------------------------------------------------------- showDialogConfirm

}