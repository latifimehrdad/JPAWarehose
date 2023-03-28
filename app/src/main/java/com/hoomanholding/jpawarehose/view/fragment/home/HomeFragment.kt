package com.hoomanholding.jpawarehose.view.fragment.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentHomeBinding
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.view.dialog.ConfirmDialog
import com.zar.core.tools.BiometricTools
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class HomeFragment(override var layout: Int  = R.layout.fragment_home) :
    JpaFragment<FragmentHomeBinding>() {

    private val homeViewModel : HomeViewModel by viewModels()

    @Inject
    lateinit var biometricTools: BiometricTools


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = homeViewModel
        binding.switchActive.isChecked = homeViewModel.isBiometricEnable()
        backClickControl()
        observeLiveData()
        setListener()
        homeViewModel.getUserInfo()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage



    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        homeViewModel.userLogOut.observe(viewLifecycleOwner) {
            (activity as MainActivity).gotoFirstFragment()
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.textViewLogout.setOnClickListener {
            val click = object : ConfirmDialog.Click {
                override fun clickYes() {
                    binding.textViewLogout.text = getText(R.string.bePatient)
                    homeViewModel.logOut()
                }
            }
            ConfirmDialog(
                requireContext(),
                getString(R.string.doYouWantToExitAccount),
                click
            ).show()
        }

        binding.switchActive.setOnClickListener { showBiometricDialog() }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- backClickControl
    private fun backClickControl() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    context?.let {
                        ConfirmDialog(
                            it,
                            getString(R.string.doYouWantToExitApp),
                            object : ConfirmDialog.Click {
                                override fun clickYes() {
                                    activity?.finish()
                                }
                            }).show()
                    }
                }
            })
    }
    //---------------------------------------------------------------------------------------------- backClickControl



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
                    binding.switchActive.isChecked = homeViewModel.isBiometricEnable()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    binding.switchActive.isChecked = homeViewModel.isBiometricEnable()
                }


                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    binding.switchActive.isChecked = !homeViewModel.isBiometricEnable()
                    homeViewModel.changeBiometricEnable()
                    showMessage(getString(R.string.actionIsDone))
                }
            })
        biometricTools.checkDeviceHasBiometric(biometricPrompt)
    }
    //---------------------------------------------------------------------------------------------- showBiometricDialog


}