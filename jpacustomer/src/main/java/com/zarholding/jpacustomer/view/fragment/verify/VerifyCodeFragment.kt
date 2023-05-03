package com.zarholding.jpacustomer.view.fragment.verify

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentVerifyCodeBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 5/3/2023
 */

@AndroidEntryPoint
class VerifyCodeFragment(override var layout: Int = R.layout.fragment_verify_code
): JpaFragment<FragmentVerifyCodeBinding>() {

    private val viewModel: VerifyCodeViewModel by viewModels()

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
        observeLiveDate()
        viewModel.startTimer()
    }
    //---------------------------------------------------------------------------------------------- initView



    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.timerLiveData.observe(viewLifecycleOwner){
            binding.mlAnimationTimer.changeTime(it)
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




    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

    }
    //---------------------------------------------------------------------------------------------- setListener


}