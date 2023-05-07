package com.zarholding.jpacustomer.view.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import dagger.hilt.android.AndroidEntryPoint
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentHomeBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class HomeFragment(override var layout: Int = R.layout.fragment_home) :
    JpaFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()

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
        resetSteps()
        testStep()
    }
    //---------------------------------------------------------------------------------------------- initView



    //---------------------------------------------------------------------------------------------- resetSteps
    private fun resetSteps(){
        binding.stepOrderConfirm.clearSelected()
        binding.stepOrderPreparation.clearSelected()
        binding.stepReviewQueue.clearSelected()
        binding.stepSendToCustomer.clearSelected()
    }
    //---------------------------------------------------------------------------------------------- resetSteps



    private fun testStep() {

        CoroutineScope(Main).launch {
            delay(2000)
            binding.stepReviewQueue.selected()
            delay(1000)
            binding.stepOrderConfirm.selected()
            delay(1000)
            binding.stepOrderPreparation.selected()
        }
    }

}