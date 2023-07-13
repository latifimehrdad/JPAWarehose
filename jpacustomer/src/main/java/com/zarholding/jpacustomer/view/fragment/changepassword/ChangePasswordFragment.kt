package com.zarholding.jpacustomer.view.fragment.changepassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.model.data.enums.EnumVerifyType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentChangePasswordBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 5/3/2023
 */

@AndroidEntryPoint
class ChangePasswordFragment(
    override var layout: Int = R.layout.fragment_change_password
) : JpaFragment<FragmentChangePasswordBinding>() {

    private val viewModel: ChangePasswordViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initView()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        viewModel.setVerifyTypeFromBundle(arguments)
        if (viewModel.verifyType == EnumVerifyType.ForgetPass)
            binding.cardViewOldPassword.visibility = View.INVISIBLE
        else
            binding.cardViewOldPassword.visibility = View.VISIBLE
        observeLiveDate()
        getTokenFromArgument()
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

        viewModel.oldPasswordError.observe(viewLifecycleOwner) {
            binding.buttonChangePassword.stopLoading()
            binding.editTextOldPassword.error = it
        }

        viewModel.passwordError.observe(viewLifecycleOwner) {
            binding.buttonChangePassword.stopLoading()
            binding.editTextPassword.error = it
        }

        viewModel.rePasswordError.observe(viewLifecycleOwner) {
            binding.editTextRePassword.text.clear()
            binding.buttonChangePassword.stopLoading()
            binding.editTextRePassword.error = it
        }

        viewModel.passwordChangeLiveData.observe(viewLifecycleOwner) {
            (activity as MainActivity?)?.gotoFirstFragment(true)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.buttonChangePassword.stopLoading()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- getTokenFromArgument
    private fun getTokenFromArgument() {
        arguments?.let { bundle ->
            val token = bundle.getString(CompanionValues.TOKEN, null)
            token?.let {
                viewModel.token = it
            } ?: run {
                (activity as MainActivity?)?.gotoFirstFragment()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getTokenFromArgument


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

        binding.buttonChangePassword.setOnClickListener {
            if (context == null || binding.buttonChangePassword.isLoading)
                return@setOnClickListener
            binding.buttonChangePassword.startLoading(getString(R.string.bePatient))
            viewModel.changePassword()
        }

    }
    //---------------------------------------------------------------------------------------------- setListener
}