package com.hoomanholding.jpamanager.view.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentProfileBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.view.dialog.ConfirmDialog
import com.zar.core.tools.BiometricTools
import javax.inject.Inject


/**
 * create by m-latifi on 3/6/2023
 */

@AndroidEntryPoint
class ProfileFragment(override var layout: Int = R.layout.fragment_profile) :
    JpaFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var biometricTools: BiometricTools

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.switchActive.isChecked = viewModel.isBiometricEnable()
        observeLiveDate()
        setListener()
        viewModel.getUserInfo()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.switchActive.setOnClickListener { showBiometricDialog() }

        binding.linearLayoutSignOut.setOnClickListener {
            if (context == null)
                return@setOnClickListener
            val click = object : ConfirmDialog.Click {
                override fun clickYes() {
                    if (activity == null)
                        return
                    (activity as MainActivity).gotoFirstFragment()
                }
            }
            ConfirmDialog(
                requireContext(),
                getString(R.string.doYouWantToExitAccount),
                click
            ).show()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- showBiometricDialog
    private fun showBiometricDialog() {
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(
            requireActivity(),
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showMessage(getString(R.string.onAuthenticationError))
                    binding.switchActive.isChecked = viewModel.isBiometricEnable()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    binding.switchActive.isChecked = viewModel.isBiometricEnable()
                }


                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    binding.switchActive.isChecked = !viewModel.isBiometricEnable()
                    viewModel.changeBiometricEnable()
                    showMessage(getString(R.string.actionIsDone))
                }
            })
        biometricTools.checkDeviceHasBiometric(biometricPrompt)
    }
    //---------------------------------------------------------------------------------------------- showBiometricDialog


}