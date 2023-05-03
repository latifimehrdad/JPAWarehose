package com.hoomanholding.applibrary.di

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * create by m-latifi on 5/3/2023
 */

@Singleton
class ApplicationInfoProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    //---------------------------------------------------------------------------------------------- getAppLabel
    @Suppress("DEPRECATION")
    fun getAppLabel(): String {
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo =
                context.packageManager.getApplicationInfo(context.applicationInfo.packageName, 0)
        } catch (_: PackageManager.NameNotFoundException) {
        }
        return (
                if (applicationInfo != null)
                    context.packageManager.getApplicationLabel(applicationInfo).toString()
                else "Jpa"
                )
    }
    //---------------------------------------------------------------------------------------------- getAppLabel

}