package com.hoomanholding.applibrary.tools

import android.animation.ValueAnimator
import com.facebook.shimmer.Shimmer


/**
 * create by m-latifi on 4/4/2023
 */

fun getShimmerBuild() = Shimmer.AlphaHighlightBuilder()
    .setDirection(Shimmer.Direction.RIGHT_TO_LEFT)
    .setDuration(1500L)
    .setRepeatMode(ValueAnimator.REVERSE)
    .setTilt(0f)
    .build()