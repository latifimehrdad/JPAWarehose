package com.zarholding.jpacustomer.view.fragment.splash

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.applibrary.ext.isIP
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.PermissionManager
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import dagger.hilt.android.AndroidEntryPoint
import com.zar.core.enums.EnumApiError
import com.zar.core.tools.manager.DialogManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentSplashBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class SplashFragment(override var layout: Int = R.layout.fragment_splash) :
    JpaFragment<FragmentSplashBinding>() {

    private val viewModel: CustomerSplashViewModel by viewModels()

    @Inject
    lateinit var permissionManager: PermissionManager


    //---------------------------------------------------------------------------------------------- notificationPermissionLauncher
    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            permissionManager.checkPermissionResult(results) {
                if (it)
                    requestGetAppVersion()
            }
        }
    //---------------------------------------------------------------------------------------------- notificationPermissionLauncher


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        observeLiveDate()
        setListener()
        checkNotificationPermission()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.cardViewLogin.setOnClickListener {
            checkNotificationPermission()
        }

        binding.imageViewLogo.setOnLongClickListener {
            if (context != null) {
                val dialog = DialogManager().createDialogHeightWrapContent(
                    requireContext(),
                    R.layout.dialog_confirm_ip,
                    Gravity.CENTER,
                    0
                )

                val textInputEditTextIp =
                    dialog.findViewById<TextInputEditText>(R.id.textInputEditTextIp)

                val textInputEditTextIpPassword =
                    dialog.findViewById<TextInputEditText>(R.id.textInputEditTextIpPassword)

                val buttonNo = dialog.findViewById<MaterialButton>(R.id.buttonNo)
                buttonNo.setOnClickListener { dialog.dismiss() }

                val buttonYes = dialog.findViewById<MaterialButton>(R.id.buttonYes)
                buttonYes.setOnClickListener {

                    if (textInputEditTextIp.text.toString().isIP()) {
                        if (textInputEditTextIpPassword.text.toString() != "holeshdaf") {
                            textInputEditTextIpPassword.error =
                                getString(R.string.passwordIsInCorrect)
                            return@setOnClickListener
                        }
                        viewModel.saveNewIp(textInputEditTextIp.text.toString())
                        showMessage(getString(R.string.updateIsSuccess))
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1500)
                            (activity as MainActivity?)?.finish()
                        }
                    } else {
                        textInputEditTextIp.error = getString(R.string.ipIsIncorrect)
                    }
                }
                dialog.show()
            }
            return@setOnLongClickListener true
        }

    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- checkNotificationPermission
    private fun checkNotificationPermission() {
        if (context == null)
            return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = listOf(Manifest.permission.POST_NOTIFICATIONS)
            val check = permissionManager.isPermissionGranted(
                permissions = permission,
                launcher = notificationPermissionLauncher
            )
            if (check) requestGetAppVersion()
        } else requestGetAppVersion()
    }
    //---------------------------------------------------------------------------------------------- checkNotificationPermission


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.successLiveData.observe(viewLifecycleOwner) {
            viewModel.subscribeToTopic()
        }

        viewModel.userIsEnteredLiveData.observe(viewLifecycleOwner) {
            if (it)
                getFireBaseToken()
            else
                gotoFragmentLogin()
        }

        viewModel.downloadVersionLiveData.observe(viewLifecycleOwner) {
            storagePermission(it)
        }

        viewModel.subscribeToTopic.observe(viewLifecycleOwner) {
            showMessage(getString(R.string.loginIsSuccess))
            if (it)
                gotoFragment(R.id.action_splashFragment_to_homeFragment)
        }

        viewModel.fireBaseTokenLiveData.observe(viewLifecycleOwner) {
            viewModel.requestGetData()
        }

        viewModel.appDescriptionLiveData.observe(viewLifecycleOwner) {
            binding.textViewComment.text = it
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- requestGetAppVersion
    private fun requestGetAppVersion() {
        viewModel.requestGetAppVersion(EnumSystemType.Customers.name)
    }
    //---------------------------------------------------------------------------------------------- requestGetAppVersion


    //---------------------------------------------------------------------------------------------- storagePermissionLauncher
    private val storagePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            permissionManager.checkPermissionResult(results) {
                if (it && viewModel.downloadVersionLiveData.value != null)
                    showDialogUpdateAppVersion(viewModel.downloadVersionLiveData.value!!)
            }
        }
    //---------------------------------------------------------------------------------------------- storagePermissionLauncher


    //---------------------------------------------------------------------------------------------- cameraPermission
    private fun storagePermission(fileName: String) {
        if (context == null)
            return
        val availableBlocks = viewModel.getInternalMemoryFreeSize()
        if (availableBlocks < 200) {
            showMessage(getString(R.string.internalMemoryIsFull))
            return
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            val permissions = listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val check = permissionManager.isPermissionGranted(
                permissions = permissions,
                launcher = storagePermissionLauncher
            )
            if (check)
                showDialogUpdateAppVersion(fileName)
        } else
            showDialogUpdateAppVersion(fileName)
    }
    //---------------------------------------------------------------------------------------------- cameraPermission


    //---------------------------------------------------------------------------------------------- showDialogUpdateAppVersion
    private fun showDialogUpdateAppVersion(fileName: String) {
        if (context == null)
            return
        ConfirmDialog(
            requireContext(),
            getString(R.string.doYouWantToUpdateApp),
            true
        ) { gotoFragmentDownload(fileName) }.show()
    }
    //---------------------------------------------------------------------------------------------- showDialogUpdateAppVersion


    //---------------------------------------------------------------------------------------------- gotoFragmentDownload
    private fun gotoFragmentDownload(fileName: String) {
        val bundle = Bundle()
        bundle.putString(CompanionValues.DOWNLOAD_URL, fileName)
        bundle.putString(CompanionValues.APP_NAME, EnumSystemType.Customers.name)
        gotoFragment(R.id.action_splashFragment_to_DownloadFragment, bundle)
    }
    //---------------------------------------------------------------------------------------------- gotoFragmentDownload


    //---------------------------------------------------------------------------------------------- gotoFragmentLogin
    private fun gotoFragmentLogin() {
        gotoFragment(R.id.action_splashFragment_to_loginFragment)
    }
    //---------------------------------------------------------------------------------------------- gotoFragmentLogin


    //---------------------------------------------------------------------------------------------- getFireBaseToken
    private fun getFireBaseToken() {
        binding.textViewLogin.text = getString(R.string.bePatient)
        viewModel.fireBaseToken()
    }
    //---------------------------------------------------------------------------------------------- getFireBaseToken


    /*    //---------------------------------------------------------------------------------------------- onDestroyView
        override fun onDestroyView() {
            super.onDestroyView()
            job?.cancel()
        }
        //---------------------------------------------------------------------------------------------- onDestroyView*/
}