package com.zarholding.jpacustomer.view.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.zarholding.jpacustomer.databinding.DialogAlertBinding
import com.zarholding.jpacustomer.view.activity.MainActivity


class AlertDialog(
    private val title: String,
    private val onClick: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogAlertBinding


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAlertBinding.inflate(inflater, container, false)
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.enableBlurView()
        val lp = WindowManager.LayoutParams()
        val window = dialog?.window
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 50)
        window?.setBackgroundDrawable(inset)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = lp
        binding.textViewTitle.text = title
        setListener()
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.buttonYes.setOnClickListener {
            onClick.invoke()
            dismiss()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- onDismiss
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as MainActivity?)?.disableBlurView()
    }
    //---------------------------------------------------------------------------------------------- onDismiss

}