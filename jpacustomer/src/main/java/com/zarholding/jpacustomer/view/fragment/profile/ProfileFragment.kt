package com.zarholding.jpacustomer.view.fragment.profile

import android.Manifest
import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.fragment.app.viewModels
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.hoomanholding.applibrary.ext.downloadProfileImage
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zar.core.enums.EnumApiError
import com.zar.core.tools.BiometricTools
import com.zar.core.tools.manager.ThemeManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentProfileBinding
import com.zarholding.jpacustomer.model.ShowImageModel
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog
import com.zarholding.jpacustomer.view.dialog.ShowImageDialog
import com.zarholding.jpacustomer.view.dialog.location.EditLocationDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by m-latifi on 5/23/2023.
 */

@AndroidEntryPoint
class ProfileFragment(
    override var layout: Int = R.layout.fragment_profile
) : JpaFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var themeManagers: ThemeManager

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var biometricTools: BiometricTools


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
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.switchFingerPrint.isChecked = viewModel.isBiometricEnable()
        val alpha =
            AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)
        binding.imageViewLocation.startAnimation(alpha)
        checkTheme()
        observeLiveDate()
        setListener()
        viewModel.getUserInfo()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.userInfoLiveData.observe(viewLifecycleOwner) {
            setUserInfo(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- checkTheme
    private fun checkTheme() {
        CoroutineScope(Main).launch {
            delay(300)
            when (themeManagers.applicationTheme()) {
                Configuration.UI_MODE_NIGHT_YES ->
                    binding.dayNightSwitch.setDayChecked(false, animated = true)

                Configuration.UI_MODE_NIGHT_NO ->
                    binding.dayNightSwitch.setDayChecked(true, animated = true)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- checkTheme


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.switchFingerPrint.setOnClickListener { showBiometricDialog() }

        binding.dayNightSwitch.setOnSwitchListener { _, _ ->
            changeAppTheme()
        }
        binding.switchActive.setOnClickListener { changeAppTheme() }
        binding.cardViewSingOut.setOnClickListener { signOut() }
        binding.textViewMyState.setOnClickListener {
            gotoFragment(R.id.action_profileFragment_to_myStateFragment)
        }
        binding.textViewVideo.setOnClickListener {
            gotoFragment(R.id.action_profileFragment_to_videoFragment)
        }
        binding.textViewContactUs.setOnClickListener {
            gotoFragment(R.id.action_profileFragment_to_aboutFragment)
        }

        binding.cardViewProfile.setOnClickListener {
            cameraPermission()
        }

        binding.imageViewLocation.setOnClickListener {
            EditLocationDialog().show(childFragmentManager, "location")
        }

        binding.textViewSuggestions.setOnClickListener {
            showMessage(getString(R.string.disableFeature))
        }

        binding.textViewHistory.setOnClickListener {
            showMessage(getString(R.string.disableFeature))
        }

    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- signOut
    private fun signOut() {
        if (context == null)
            return
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
    //---------------------------------------------------------------------------------------------- signOut


    //---------------------------------------------------------------------------------------------- changeAppTheme
    private fun changeAppTheme() {
        CoroutineScope(IO).launch {
            delay(300)
            withContext(Main) {
                if (!binding.dayNightSwitch.isDayChecked())
                    themeManagers.changeApplicationTheme(Configuration.UI_MODE_NIGHT_YES)
                else
                    themeManagers.changeApplicationTheme(Configuration.UI_MODE_NIGHT_NO)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- changeAppTheme


    //---------------------------------------------------------------------------------------------- setUserInfo
    private fun setUserInfo(userInfo: UserInfoEntity) {
        binding.textViewName.text = userInfo.fullName
        binding.textViewVisitorName.text = userInfo.visitorName
        binding.textViewUserCode.setTitleAndValue(
            title = getString(R.string.customerCode),
            splitter = getString(R.string.colon),
            value = userInfo.personnelNumber
        )
        binding.textViewUserMobile.setTitleAndValue(
            title = getString(R.string.mobileNumber),
            splitter = getString(R.string.colon),
            value = userInfo.mobileNumber
        )
        binding.textViewVisitorMobile.setTitleAndValue(
            title = getString(R.string.mobileNumber),
            splitter = getString(R.string.colon),
            value = userInfo.visitorMobile
        )
        binding.textViewShopName.setTitleAndValue(
            title = getString(R.string.shopName),
            splitter = getString(R.string.colon),
            value = userInfo.storeName
        )
        binding.textViewShopAddress.setTitleAndValue(
            title = getString(R.string.shopAddress),
            splitter = getString(R.string.colon),
            value = userInfo.address
        )
        binding.imageViewProfile.downloadProfileImage(
            url = userInfo.profileImageName,
            systemType = EnumSystemType.Customers.name,
            token = viewModel.getBearerToken()
        )
        binding.imageViewVisitorImage.downloadProfileImage(
            url = userInfo.visitorImageName,
            systemType = EnumSystemType.Customers.name,
            token = viewModel.getBearerToken(),
            entityType = EnumEntityType.VisitorImage.name
        )
        binding.cardViewVisitorProfile.setOnClickListener {
            if (context == null)
                return@setOnClickListener
            val model = ShowImageModel(
                userInfo.visitorImageName,
                EnumEntityType.VisitorImage.name,
                viewModel.getBearerToken()
            )
            ShowImageDialog(requireContext(), model).show()
        }
    }
    //---------------------------------------------------------------------------------------------- setUserInfo


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
        viewModel.uploadPercentLiveData.observe(viewLifecycleOwner) {
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
                    binding.switchFingerPrint.isChecked = viewModel.isBiometricEnable()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    binding.switchFingerPrint.isChecked = viewModel.isBiometricEnable()
                }


                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    binding.switchFingerPrint.isChecked = !viewModel.isBiometricEnable()
                    viewModel.changeBiometricEnable()
                    showMessage(getString(R.string.actionIsDone))
                }
            })
        biometricTools.checkDeviceHasBiometric(biometricPrompt)
    }
    //---------------------------------------------------------------------------------------------- showBiometricDialog


}