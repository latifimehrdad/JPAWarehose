package com.zarholding.jpacustomer.view.fragment.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.applibrary.ext.hideKeyboard
import com.hoomanholding.applibrary.ext.isIP
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.zar.core.tools.BiometricTools
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.applibrary.view.fragment.LoginViewModel
import com.zar.core.tools.manager.DialogManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentLoginBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by m-latifi on 11/9/2022.
 */

@AndroidEntryPoint
class LoginFragment(override var layout: Int = R.layout.fragment_login) :
    JpaFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var biometricTools: BiometricTools

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
            showBiometricDialog()
        }
        binding.checkBoxSave.isChecked = loginViewModel.isBiometricEnable()
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
            it?.let {
                val bundle = Bundle()
                bundle.putString(CompanionValues.TOKEN, it)
                gotoFragment(R.id.action_loginFragment_to_verifyCodeFragment, bundle)
            }
        }

        loginViewModel.userNameError.observe(viewLifecycleOwner) {
            binding.editTextUserName.error = it
            stopLoading()
        }

        loginViewModel.passwordError.observe(viewLifecycleOwner) {
            binding.editTextPassword.error = it
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

        binding.checkBoxSave.setOnClickListener {
            loginViewModel.changeBiometricEnable(binding.checkBoxSave.isChecked)
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
                        loginViewModel.saveNewIp(textInputEditTextIp.text.toString())
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

        /*        binding
                    .cardViewFingerPrint
                    .setOnClickListener { showBiometricDialog() }*/
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- showBiometricDialog
    private fun showBiometricDialog() {
        if (activity == null)
            return
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(
            requireActivity(),
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showMessage(getString(R.string.onAuthenticationError))
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    fingerPrintClick()
                }
            })
        biometricTools.checkDeviceHasBiometric(biometricPrompt)
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
        loginViewModel.login(fromFingerPrint, androidId, EnumSystemType.Customers)
    }
    //---------------------------------------------------------------------------------------------- login


    //---------------------------------------------------------------------------------------------- startLoading
    private fun startLoading() {
        hideKeyboard()
        binding.editTextUserName.error = null
        binding.editTextPassword.error = null
        binding.editTextUserName.isEnabled = false
        binding.editTextPassword.isEnabled = false
        binding.buttonLogin.startLoading(getString(R.string.bePatient))
    }
    //---------------------------------------------------------------------------------------------- startLoading


    //---------------------------------------------------------------------------------------------- stopLoading
    private fun stopLoading() {
        binding.editTextUserName.isEnabled = true
        binding.editTextPassword.isEnabled = true
        binding.buttonLogin.stopLoading()
    }
    //---------------------------------------------------------------------------------------------- stopLoading


}