package com.zarholding.jpacustomer.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.zarholding.jpacustomer.R

class SubmitBasketDialog(
    context: Context,
    private val onClick: (String) -> Unit
) : Dialog(context) {


    //---------------------------------------------------------------------------------------------- onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_submit_basket)
        val lp = WindowManager.LayoutParams()
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        this.window?.setGravity(Gravity.CENTER)
        lp.copyFrom(this.window?.attributes)
        lp.horizontalMargin = 50f
        this.window?.attributes = lp
    }
    //---------------------------------------------------------------------------------------------- onCreate


    //---------------------------------------------------------------------------------------------- onStart
    override fun onStart() {
        initDialog()
        super.onStart()
    }
    //---------------------------------------------------------------------------------------------- onStart


    //---------------------------------------------------------------------------------------------- initDialog
    private fun initDialog() {
        val textInputEditTextDescription = this.findViewById<TextView>(R.id.textInputEditTextDescription)
        val buttonYes = this.findViewById<MaterialButton>(R.id.buttonYes)
        val buttonNo = this.findViewById<MaterialButton>(R.id.buttonNo)

        buttonYes.setOnClickListener {
            if (textInputEditTextDescription.text.isNullOrEmpty()) {
                textInputEditTextDescription.error =
                    context.getString(R.string.descriptionIsEmpty)
                return@setOnClickListener
            }
            onClick(textInputEditTextDescription.text.toString())
            dismiss()
        }

        buttonNo.setOnClickListener { dismiss() }

    }
    //---------------------------------------------------------------------------------------------- initDialog

}