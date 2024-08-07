package com.hoomanholding.jpawarehose.view.fragment.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.applibrary.ext.hideKeyboard
import com.hoomanholding.applibrary.ext.isIP
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.tools.BiometricManager
import com.hoomanholding.applibrary.view.fragment.LoginViewModel
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentLoginBinding
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.view.dialog.ConfirmDialog
import com.zar.core.tools.manager.DialogManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 11/9/2022.
 */

@AndroidEntryPoint
class LoginFragment(override var layout: Int = R.layout.fragment_login) :
    JpaFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var biometricManager: BiometricManager

    private val loginViewModel: LoginViewModel by viewModels()


    //---------------------------------------------------------------------------------------------- OnBackPressedCallback
    private val backClick = object : OnBackPressedCallback(true) {
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
    }
    //---------------------------------------------------------------------------------------------- OnBackPressedCallback



    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = loginViewModel
        initView()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backClick)
        activity?.let { (it as MainActivity).deleteAllData() }
        if (loginViewModel.isBiometricEnable()) {
            binding.cardViewFingerPrint.visibility = View.VISIBLE
            binding.viewLine1.visibility = View.VISIBLE
            binding.viewLine2.visibility = View.VISIBLE
        } else {
            binding.cardViewFingerPrint.visibility = View.INVISIBLE
            binding.viewLine1.visibility = View.GONE
            binding.viewLine2.visibility = View.GONE
        }
        observeLiveDate()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        loginViewModel.errorLiveDate.observe(viewLifecycleOwner) {
            binding.buttonLogin.stopLoading()
            showMessage(it.message)
        }

        loginViewModel.loginLiveDate.observe(viewLifecycleOwner) {
            backClick.isEnabled = false
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        loginViewModel.userNameError.observe(viewLifecycleOwner) {
            binding.textInputLayoutUserName.error = it
            stopLoading()
        }

        loginViewModel.passwordError.observe(viewLifecycleOwner) {
            binding.textInputLayoutPasscode.error = it
            stopLoading()
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
        stopLoading()
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

        binding
            .buttonLogin
            .setOnClickListener { login(false) }

        binding
            .cardViewFingerPrint
            .setOnClickListener { showBiometricDialog() }

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
                            showMessage(getString(R.string.passwordIsInCorrect))
                            return@setOnClickListener
                        }
                        loginViewModel.saveNewIp(textInputEditTextIp.text.toString())
                        showMessage(getString(R.string.updateIsSuccess))
                        CoroutineScope(Main).launch {
                            delay(1500)
                            (activity as MainActivity?)?.finish()
                        }
                    } else {
                        textInputEditTextIp.error = getString(R.string.ipIsIncorrect)
                        showMessage(getString(R.string.ipIsIncorrect))
                    }
                }
                dialog.show()
            }
            return@setOnLongClickListener true
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- showBiometricDialog
    private fun showBiometricDialog() {
        if (activity == null)
            return
        biometricManager.showBiometricDialog(
            fragment = requireActivity(),
            onAuthenticationError = { showMessage(getString(R.string.onAuthenticationError)) },
            onAuthenticationSucceeded = { fingerPrintClick() }
        )
    }
    //---------------------------------------------------------------------------------------------- showBiometricDialog


    //---------------------------------------------------------------------------------------------- fingerPrintClick
    private fun fingerPrintClick() {
        login(true)
    }
    //---------------------------------------------------------------------------------------------- fingerPrintClick


    //---------------------------------------------------------------------------------------------- login
    @SuppressLint("HardwareIds")
    private fun login(fromFingerPrint: Boolean) {
        if (context == null || binding.buttonLogin.isLoading)
            return
        startLoading()
        val androidId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        loginViewModel.login(fromFingerPrint, androidId, EnumSystemType.WareHouse)
    }
    //---------------------------------------------------------------------------------------------- login



    //---------------------------------------------------------------------------------------------- startLoading
    private fun startLoading() {
        hideKeyboard()
        binding.textInputLayoutUserName.error = null
        binding.textInputLayoutPasscode.error = null
        binding.textInputEditTextUserName.isEnabled = false
        binding.textInputEditTextPasscode.isEnabled = false
        binding.buttonLogin.startLoading(getString(R.string.bePatient))
    }
    //---------------------------------------------------------------------------------------------- startLoading


    //---------------------------------------------------------------------------------------------- stopLoading
    private fun stopLoading() {
        binding.textInputEditTextUserName.isEnabled = true
        binding.textInputEditTextPasscode.isEnabled = true
        binding.buttonLogin.stopLoading()
    }
    //---------------------------------------------------------------------------------------------- stopLoading


}