package com.hoomanholding.jpamanager.view.fragment.report.customer.check

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.report.CustomerBounceCheckReportModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentCustomerBounceCheckBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.recycler.CustomerBounceCheckAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 5/1/2023
 */

@AndroidEntryPoint
class CustomerBounceCheckFragment(
    override var layout: Int = R.layout.fragment_customer_bounce_check
): JpaFragment<FragmentCustomerBounceCheckBinding>() {


    private val viewModel : CustomerBounceCheckViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveData()
        getReport()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopLoading()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.itemLiveData.observe(viewLifecycleOwner) {
            setItemAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData



    //---------------------------------------------------------------------------------------------- getReport
    private fun getReport() {
        binding.shimmerViewContainer.startLoading()
        viewModel.requestCustomersBouncedCheckReport()
    }
    //---------------------------------------------------------------------------------------------- getReport



    //---------------------------------------------------------------------------------------------- setItemAdapter
    private fun setItemAdapter(items: List<CustomerBounceCheckReportModel>) {
        binding.shimmerViewContainer.stopLoading()
        val adapter = CustomerBounceCheckAdapter(items)
        val linearLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerItem.layoutManager = linearLayoutManager
        binding.recyclerItem.layoutDirection = View.LAYOUT_DIRECTION_RTL
        binding.recyclerItem.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setItemAdapter


}