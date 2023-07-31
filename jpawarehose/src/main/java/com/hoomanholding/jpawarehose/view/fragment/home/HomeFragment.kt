package com.hoomanholding.jpawarehose.view.fragment.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.tools.BiometricManager
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentHomeBinding
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.view.dialog.ConfirmDialog
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
    lateinit var biometricManager: BiometricManager


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
        homeViewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        homeViewModel.userLogOut.observe(viewLifecycleOwner) {
            (activity as MainActivity?)?.gotoFirstFragment()
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
        if (activity == null)
            return
        biometricManager.showBiometricDialog(
            fragment = requireActivity(),
            onAuthenticationError = {
                showMessage(getString(R.string.onAuthenticationError))
                binding.switchActive.isChecked = homeViewModel.isBiometricEnable()
            },
            onAuthenticationSucceeded = {
                binding.switchActive.isChecked = !homeViewModel.isBiometricEnable()
                homeViewModel.changeBiometricEnable()
                showMessage(getString(R.string.actionIsDone))
            }
        )
    }
    //---------------------------------------------------------------------------------------------- showBiometricDialog


}