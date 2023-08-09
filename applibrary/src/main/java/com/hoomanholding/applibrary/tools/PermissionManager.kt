package com.hoomanholding.applibrary.tools

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by m-latifi on 8/9/2023.
 */

@Singleton
class PermissionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {


    //---------------------------------------------------------------------------------------------- isPermissionGranted
    fun isPermissionGranted(
        permissions: List<String>,
        launcher: ActivityResultLauncher<Array<String>>
    ): Boolean {

        val listPermissionsNeeded = mutableListOf<String>()

        for (permission in permissions) {
            val check = ContextCompat.checkSelfPermission(context, permission)
            if (check != PackageManager.PERMISSION_GRANTED)
                listPermissionsNeeded.add(permission)
        }
        return if (listPermissionsNeeded.isEmpty())
            true
        else {
            launcher.launch(listPermissionsNeeded.toList().toTypedArray())
            false
        }
    }
    //---------------------------------------------------------------------------------------------- isPermissionGranted


    //---------------------------------------------------------------------------------------------- checkPermissionResult
    fun checkPermissionResult(results: Map<String, Boolean>, onCheck: (Boolean) -> Unit) {
        var check = true
        for (result in results)
            if (!result.value)
                check = false
        onCheck(check)
    }
    //---------------------------------------------------------------------------------------------- checkPermissionResult

}

