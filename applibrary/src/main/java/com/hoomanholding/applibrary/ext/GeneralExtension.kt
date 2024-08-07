package com.hoomanholding.applibrary.ext

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/**
 * Created by m-latifi on 01/04/2023.
 */

//-------------------------------------------------------------------------------------------------- hideKeyboard
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
//-------------------------------------------------------------------------------------------------- hideKeyboard


//-------------------------------------------------------------------------------------------------- isIP
fun String?.isIP() = this?.matches(Regex(
    "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
)) ?: false
//-------------------------------------------------------------------------------------------------- isIP
