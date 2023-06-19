package com.zarholding.jpacustomer.view.fragment.splash

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import dagger.hilt.android.AndroidEntryPoint
import com.hoomanholding.applibrary.view.fragment.SplashViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentSplashBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class SplashFragment(override var layout: Int = R.layout.fragment_splash) :
    JpaFragment<FragmentSplashBinding>() {

    private val splashViewModel: SplashViewModel by viewModels()
    private val customerSplashViewModel: CustomerSplashViewModel by viewModels()
//    var job: Job? = null

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
        requestGetAppVersion()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.cardViewLogin.setOnClickListener {
            requestGetAppVersion()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        splashViewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        splashViewModel.successLiveData.observe(viewLifecycleOwner) {
            customerSplashViewModel.fireBaseToken()
        }

        splashViewModel.userIsEnteredLiveData.observe(viewLifecycleOwner) {
            if (it)
                gotoFragmentHome()
            else
                gotoFragmentLogin()
        }

        splashViewModel.downloadVersionLiveData.observe(viewLifecycleOwner) {
            storagePermission(it)
        }

        customerSplashViewModel.subscribeToTopic.observe(viewLifecycleOwner) {
            showMessage(getString(R.string.loginIsSuccess))
            if (it)
                gotoFragment(R.id.action_splashFragment_to_homeFragment)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- requestGetAppVersion
    private fun requestGetAppVersion() {
        splashViewModel.requestGetAppVersion(EnumSystemType.Customers.name)
    }
    //---------------------------------------------------------------------------------------------- requestGetAppVersion



    //---------------------------------------------------------------------------------------------- cameraPermission
    private fun storagePermission(fileName: String) {
        if (context == null)
            return
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
            object : ConfirmDialog.Click {
                override fun clickYes() {
                    gotoFragmentDownload(fileName)
                }
            }, true
        ).show()
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


    //---------------------------------------------------------------------------------------------- gotoFragmentHome
    private fun gotoFragmentHome() {
        binding.textViewLogin.text = getString(R.string.bePatient)
        splashViewModel.requestGetData()
    }
    //---------------------------------------------------------------------------------------------- gotoFragmentHome


/*    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }
    //---------------------------------------------------------------------------------------------- onDestroyView*/
}