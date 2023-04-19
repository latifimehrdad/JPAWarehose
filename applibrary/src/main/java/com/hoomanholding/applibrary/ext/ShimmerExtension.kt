package com.hoomanholding.applibrary.ext

import android.view.View
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout


/**
 * create by m-latifi on 4/4/2023
 */

//-------------------------------------------------------------------------------------------------- config
fun ShimmerFrameLayout.config(shimmer: Shimmer) {
    setShimmer(shimmer)
    hideShimmer()
    visibility = View.GONE
}
//-------------------------------------------------------------------------------------------------- config


//-------------------------------------------------------------------------------------------------- startLoading
fun ShimmerFrameLayout.startLoading() {
    visibility = View.VISIBLE
    showShimmer(true)
}
//-------------------------------------------------------------------------------------------------- startLoading


//-------------------------------------------------------------------------------------------------- stopLoading
fun ShimmerFrameLayout.stopLoading() {
    hideShimmer()
    visibility = View.GONE
}
//-------------------------------------------------------------------------------------------------- stopLoading