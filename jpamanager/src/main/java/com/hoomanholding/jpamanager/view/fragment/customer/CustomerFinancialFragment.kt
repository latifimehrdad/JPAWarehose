package com.hoomanholding.jpamanager.view.fragment.customer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentCustomerFinancialBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 4/17/2023
 */

@AndroidEntryPoint
class CustomerFinancialFragment(override var layout: Int = R.layout.fragment_customer_financial) :
    JpaFragment<FragmentCustomerFinancialBinding>() {

    private val viewModel: CustomerFinancialViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopShimmer()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveData()
        getCustomerIdFromArgument()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.customerFinancialLiveData.observe(viewLifecycleOwner){
            binding.shimmerViewContainer.stopShimmer()
            binding.item = it
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- getCustomerIdFromArgument
    private fun getCustomerIdFromArgument() {
        arguments?.let {
            val customerId = it.getInt(CompanionValues.CUSTOMER_ID, 0)
            if (customerId == 0){
                activity?.onBackPressedDispatcher?.onBackPressed()
                return
            }
            viewModel.customerId = customerId
            getCustomerFinancial()
        } ?: run {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }
    //---------------------------------------------------------------------------------------------- getCustomerIdFromArgument


    //---------------------------------------------------------------------------------------------- getCustomerFinancial
    private fun getCustomerFinancial() {
        binding.shimmerViewContainer.startLoading()
        viewModel.requestGetCustomerFinancial()
    }
    //---------------------------------------------------------------------------------------------- getCustomerFinancial


}