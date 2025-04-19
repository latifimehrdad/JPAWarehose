package com.hoomanholding.applibrary.view.dialog

import androidx.fragment.app.FragmentManager
import com.zar.core.view.picker.persian.DatePicker
import com.zar.core.view.picker.persian.interfaces.DateSetListener
import java.util.Calendar
import java.util.Locale

class DateDialog(
    private val action: DialogAction,
    private val manager: FragmentManager,
    private val onSingleDateSelectedListener: (String) -> Unit
) {

    interface DialogAction {
        fun onStart()

        fun onDismiss()
    }

    init {
        showDatePickerDialog2()
    }


    //---------------------------------------------------------------------------------------------- showDatePickerDialog2
    private fun showDatePickerDialog2() {
        val listener = object : DateSetListener {
            override fun onDateSet(id: Int, calendar: Calendar?, day: Int, month: Int, year: Int) {
                val mMonth = String.format(Locale.US,"%02d", month)
                val mDay = String.format(Locale.US,"%02d", day)
                onSingleDateSelectedListener.invoke(
                    "$year/$mMonth/$mDay"
                )
            }

            override fun onShowDialog() {
                action.onStart()
            }

            override fun onDismissDialog() {
                action.onDismiss()
            }

        }
        DatePicker.Builder()
            .id(7126)
            .closeYearAutomatically(true)
            .build(listener)
            .show(manager, "")
    }
    //---------------------------------------------------------------------------------------------- showDatePickerDialog2


}