package com.hoomanholding.jpamanager.view.fragment.profile

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.viewModels
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.hoomanholding.applibrary.tools.BiometricManager
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentProfileBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.dialog.ConfirmDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * create by m-latifi on 3/6/2023
 */

@AndroidEntryPoint
class ProfileFragment(override var layout: Int = R.layout.fragment_profile) :
    JpaFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var biometricManager: BiometricManager


    //---------------------------------------------------------------------------------------------- launcher
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                uri?.let { uploadProfileImage(uri) }
            }
        }
    //---------------------------------------------------------------------------------------------- launcher


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
        binding.textViewPercent.visibility = View.GONE
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.switchActive.setOnClickListener { showBiometricDialog() }

        binding.cardViewProfile.setOnClickListener {
            cameraPermission()
        }

        binding.linearLayoutSignOut.setOnClickListener {
            if (context == null)
                return@setOnClickListener
            val click = object : ConfirmDialog.Click {
                override fun clickYes() {
                    (activity as MainActivity?)?.gotoFirstFragment()
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
        if (activity == null)
            return
        val error = biometricManager.showBiometricDialog(
            fragment = requireActivity(),
            onAuthenticationError = {
                showMessage(getString(R.string.onAuthenticationError))
                binding.switchActive.isChecked = viewModel.isBiometricEnable()
            },
            onAuthenticationFailed = {
                binding.switchActive.isChecked = viewModel.isBiometricEnable()
            },
            onAuthenticationSucceeded = {
                binding.switchActive.isChecked = !viewModel.isBiometricEnable()
                viewModel.changeBiometricEnable()
                showMessage(getString(R.string.actionIsDone))
            }
        )
        if (!error.isNullOrEmpty()) {
            showMessage(error)
            binding.switchActive.isChecked = viewModel.isBiometricEnable()
        }
    }
    //---------------------------------------------------------------------------------------------- showBiometricDialog


    //---------------------------------------------------------------------------------------------- cameraPermission
    private fun cameraPermission() {
        if (context == null)
            return
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    ImagePicker
                        .Companion
                        .with(requireActivity())
                        .crop()
                        .cropSquare()
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog { launcher.launch(it) }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {

                }

            }).check()
    }
    //---------------------------------------------------------------------------------------------- cameraPermission


    //---------------------------------------------------------------------------------------------- uploadProfileImage
    private fun uploadProfileImage(pictureUri: Uri) {
        binding.textViewPercent.visibility = View.VISIBLE
        binding.imageViewProfile.setImageURI(pictureUri)
        viewModel.uploadPercentLiveData.observe(viewLifecycleOwner){
            if (it > 100) {
                binding.textViewPercent.visibility = View.GONE
                viewModel.uploadPercentLiveData.removeObservers(viewLifecycleOwner)
                getUserInfoInActivity()
            } else {
                val percent = "$it %"
                binding.textViewPercent.text = percent
            }
        }
        viewModel.uploadProfileImage(pictureUri.toFile())
    }
    //---------------------------------------------------------------------------------------------- uploadProfileImage



    //---------------------------------------------------------------------------------------------- getUserInfoInActivity
    private fun getUserInfoInActivity() {
        activity?.let {
            (it as MainActivity).getUserInfo()
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfoInActivity

}