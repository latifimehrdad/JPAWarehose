package com.hoomanholding.applibrary.tools

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.zar.core.tools.BiometricTools
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by m-latifi on 7/31/2023.
 */

class BiometricManager @Inject constructor(
    private val biometricTools: BiometricTools,
    @ApplicationContext private val context: Context
) {

    //---------------------------------------------------------------------------------------------- showBiometricDialog
    fun showBiometricDialog(
        fragment: FragmentActivity,
        onAuthenticationError: () -> Unit,
        onAuthenticationFailed: () -> Unit = {},
        onAuthenticationSucceeded: () -> Unit
    ): String? {
        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(
            fragment,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onAuthenticationError.invoke()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onAuthenticationFailed.invoke()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onAuthenticationSucceeded.invoke()
                }
            })
        return biometricTools.checkDeviceHasBiometric(biometricPrompt)
    }
    //---------------------------------------------------------------------------------------------- showBiometricDialog


}