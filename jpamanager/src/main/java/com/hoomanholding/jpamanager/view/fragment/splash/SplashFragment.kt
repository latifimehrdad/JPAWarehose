package com.hoomanholding.jpamanager.view.fragment.splash

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.applibrary.ext.isIP
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.PermissionManager
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentSplashBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.zar.core.enums.EnumApiError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.hoomanholding.jpamanager.view.dialog.ConfirmDialog
import com.zar.core.tools.manager.DialogManager
import javax.inject.Inject


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class SplashFragment(override var layout: Int = R.layout.fragment_splash) :
    JpaFragment<FragmentSplashBinding>() {

    private val viewModel: ManagerSplashViewModel by viewModels()

    @Inject
    lateinit var permissionManager: PermissionManager

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewManegerApp.visibility = View.INVISIBLE
        binding.materialButtonLogin.visibility = View.INVISIBLE
        observeLiveDate()
        setListener()
        startAnimation()
        requestGetAppVersion()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.materialButtonLogin.stopLoading()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.materialButtonLogin.setOnClickListener {
            requestGetAppVersion()
        }

        binding.frameLayoutLogo.setOnLongClickListener {
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
                        CoroutineScope(Main).launch {
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


    //---------------------------------------------------------------------------------------------- startAnimation
    private fun startAnimation() {
        CoroutineScope(Main).launch {
            delay(1000)
            if (context != null) {
                binding.imageViewManegerApp.animation = null
                binding.materialButtonLogin.animation = null
                val slideInLeft =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left)
                binding.imageViewManegerApp.startAnimation(slideInLeft)
                val slideInBottom =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_bottom)
                binding.materialButtonLogin.startAnimation(slideInBottom)
                binding.imageViewManegerApp.visibility = View.VISIBLE
                binding.materialButtonLogin.visibility = View.VISIBLE
            }
        }

        CoroutineScope(Main).launch {
            delay(2000)
            if (context != null) {
                binding.imageViewManegerApp.animation = null
                binding.imageViewManegerApp.visibility = View.INVISIBLE
                val alpha =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)
                binding.imageViewManegerApp.startAnimation(alpha)
                binding.imageViewManegerApp.visibility = View.VISIBLE
            }
        }
    }
    //---------------------------------------------------------------------------------------------- startAnimation


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
            //viewModel.subscribeToTopic()
            showMessage(getString(R.string.loginIsSuccess))
            if (it)
                gotoFragment(R.id.action_splashFragment_to_HomeFragment)
        }

        viewModel.userIsEnteredLiveData.observe(viewLifecycleOwner) {
            if (it)
                viewModel.requestGetData()//getFireBaseToken()
            else
                gotoFragmentLogin()
        }

        viewModel.downloadVersionLiveData.observe(viewLifecycleOwner) {
            storagePermission(it)
        }

        viewModel.subscribeToTopic.observe(viewLifecycleOwner) {
            showMessage(getString(R.string.loginIsSuccess))
            if (it)
                gotoFragment(R.id.action_splashFragment_to_HomeFragment)
        }

        viewModel.fireBaseTokenLiveData.observe(viewLifecycleOwner) {
            viewModel.requestGetData()
        }

/*        viewModel.appDescriptionLiveData.observe(viewLifecycleOwner) {
            binding.textViewComment.text = it
        }*/
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- requestGetAppVersion
    private fun requestGetAppVersion() {
        viewModel.requestGetAppVersion(EnumSystemType.ManagerApp.name)
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
        bundle.putString(CompanionValues.APP_NAME, EnumSystemType.ManagerApp.name)
        gotoFragment(R.id.action_splashFragment_to_DownloadFragment, bundle)
    }
    //---------------------------------------------------------------------------------------------- gotoFragmentDownload


    //---------------------------------------------------------------------------------------------- getFireBaseToken
    private fun getFireBaseToken() {
        if (binding.materialButtonLogin.isLoading)
            return
        binding.materialButtonLogin.startLoading(getString(R.string.bePatient))
        viewModel.fireBaseToken()
    }
    //---------------------------------------------------------------------------------------------- getFireBaseToken


    //---------------------------------------------------------------------------------------------- gotoFragmentLogin
    private fun gotoFragmentLogin() {
        gotoFragment(R.id.action_splashFragment_to_loginFragment)
    }
    //---------------------------------------------------------------------------------------------- gotoFragmentLogin

}