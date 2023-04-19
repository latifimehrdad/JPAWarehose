package com.hoomanholding.applibrary.di

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Create by Mehrdad on 1/7/2023
 */

@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    //---------------------------------------------------------------------------------------------- getString
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
    //---------------------------------------------------------------------------------------------- getString


    //---------------------------------------------------------------------------------------------- getString
    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return context.getString(resId, *formatArgs)
    }
    //---------------------------------------------------------------------------------------------- getString

}