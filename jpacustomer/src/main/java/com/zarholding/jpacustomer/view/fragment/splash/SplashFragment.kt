package com.zarholding.jpacustomer.view.fragment.splash

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.applibrary.ext.isIP
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import dagger.hilt.android.AndroidEntryPoint
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class SplashFragment(override var layout: Int = R.layout.fragment_splash) :
    JpaFragment<FragmentSplashBinding>() {

    private val viewModel: CustomerSplashViewModel by viewModels()

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
                        if (textInputEditTextIpPassword.text.toString() != "holeshdaf"){
                            textInputEditTextIpPassword.error = getString(R.string.passwordIsInCorrect)
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
            val permission = mutableListOf(Manifest.permission.POST_NOTIFICATIONS)
            Dexter.withContext(requireContext())
                .withPermissions(permission)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        requestGetAppVersion()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {

                    }
                })
                .check()
        } else
            requestGetAppVersion()
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



    //---------------------------------------------------------------------------------------------- cameraPermission
    private fun storagePermission(fileName: String) {
        if (context == null)
            return
        val availableBlocks = viewModel.getInternalMemoryFreeSize()
        if (availableBlocks < 200) {
            showMessage(getString(R.string.internalMemoryIsFull))
            return
        }
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    showDialogUpdateAppVersion(fileName)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {

                }

            }).check()
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
        ){ gotoFragmentDownload(fileName) }.show()
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