package com.zarholding.jpacustomer.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.zarholding.jpacustomer.R

class ConfirmDialog(
    context: Context,
    private val title: String,
    private val force: Boolean = false,
    private val onClick: () -> Unit
) : Dialog(context) {


    //---------------------------------------------------------------------------------------------- onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm)
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
        val textViewTitle = this.findViewById<TextView>(R.id.textViewTitle)
        val buttonYes = this.findViewById<MaterialButton>(R.id.buttonYes)
        val buttonNo = this.findViewById<MaterialButton>(R.id.buttonNo)
        this.setCancelable(!force)
        if (force)
            buttonNo.visibility = View.INVISIBLE
        else
            buttonNo.visibility = View.VISIBLE

        textViewTitle.text = title

        buttonYes.setOnClickListener {
            onClick()
            dismiss()
        }

        buttonNo.setOnClickListener { dismiss() }

    }
    //---------------------------------------------------------------------------------------------- initDialog

}