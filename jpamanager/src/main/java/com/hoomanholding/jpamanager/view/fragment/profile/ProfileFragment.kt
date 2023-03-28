package com.hoomanholding.jpamanager.view.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.jpamanager.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentProfileBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 3/6/2023
 */

@AndroidEntryPoint
class ProfileFragment(override var layout: Int = R.layout.fragment_profile):
    JpaFragment<FragmentProfileBinding>(){

    private val viewModel: ProfileViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        observeLiveDate()
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
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


}