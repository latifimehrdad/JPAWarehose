package com.hoomanholding.jpamanager.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.applibrary.model.data.database.entity.SupplierEntity
import com.hoomanholding.applibrary.model.data.enums.EnumState
import com.hoomanholding.applibrary.model.data.response.reason.DisApprovalReasonModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.view.adapter.ReasonSpinnerAdapter
import com.skydoves.powerspinner.PowerSpinnerView

class ConfirmOrderDialog(
    context: Context,
    private val click: Click,
    private val reasons: List<DisApprovalReasonModel>?,
    private val status: EnumState
) : Dialog(context) {

    private var position: Int = -1

    //---------------------------------------------------------------------------------------------- Click
    interface Click {
        fun clickYes(position: Int, description: String)
    }
    //---------------------------------------------------------------------------------------------- Click


    //---------------------------------------------------------------------------------------------- onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_order)
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

        val buttonYes = this.findViewById<MaterialButton>(R.id.buttonYes)
        val buttonNo = this.findViewById<MaterialButton>(R.id.buttonNo)
        val textInputEditTextDescription =
            this.findViewById<TextInputEditText>(R.id.textInputEditTextDescription)
        val powerSpinnerReason = this.findViewById<PowerSpinnerView>(R.id.powerSpinnerReason)

        if (status == EnumState.Confirmed)
            powerSpinnerReason.visibility = View.GONE
        else
            powerSpinnerReason.visibility = View.VISIBLE

        if (reasons != null) {
            powerSpinnerReason.apply {
                setSpinnerAdapter(ReasonSpinnerAdapter(this))
                setItems(reasons)
                getSpinnerRecyclerView().layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setOnSpinnerItemSelectedListener<DisApprovalReasonModel> { _, _, newIndex, _ ->
                    position = newIndex
                }
            }
        }

        buttonYes.setOnClickListener {
            if (status == EnumState.Reject && position == -1)
                return@setOnClickListener
            click.clickYes(position, textInputEditTextDescription.text.toString())
            dismiss()
        }

        buttonNo.setOnClickListener { dismiss() }

    }
    //---------------------------------------------------------------------------------------------- initDialog

}