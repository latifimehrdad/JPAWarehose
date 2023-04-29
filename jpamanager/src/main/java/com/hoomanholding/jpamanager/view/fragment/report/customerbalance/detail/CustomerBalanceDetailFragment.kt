package com.hoomanholding.jpamanager.view.fragment.report.customerbalance.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentCustomerBalanceDetailBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.recycler.CustomerBalanceDetailAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 4/29/2023
 */

@AndroidEntryPoint
class CustomerBalanceDetailFragment(
    override var layout: Int = R.layout.fragment_customer_balance_detail
) : JpaFragment<FragmentCustomerBalanceDetailBinding>() {

    private val viewModel: CustomerBalanceReportDetailViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
        binding.viewModel = viewModel
        observeLiveData()
        getReportDetail()
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

        viewModel.reportDetailLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setItemAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- getReportDetail
    private fun getReportDetail() {
        binding.shimmerViewContainer.startLoading()
        arguments?.let {
            viewModel.setValueFromArgument(it)
        }
    }
    //---------------------------------------------------------------------------------------------- getReportDetail



    //---------------------------------------------------------------------------------------------- setItemAdapter
    private fun setItemAdapter(items: List<CustomerBalanceReportDetailModel>) {
        if (context == null)
            return
        val adapter = CustomerBalanceDetailAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setItemAdapter


}