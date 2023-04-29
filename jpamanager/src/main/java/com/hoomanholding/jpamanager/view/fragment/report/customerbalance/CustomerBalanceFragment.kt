package com.hoomanholding.jpamanager.view.fragment.report.customerbalance

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentCustomerBalanceBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerBalanceHolder
import com.hoomanholding.jpamanager.view.adapter.recycler.CustomerBalanceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


/**
 * create by m-latifi on 4/29/2023
 */

@AndroidEntryPoint
class CustomerBalanceFragment(
    override var layout: Int = R.layout.fragment_customer_balance
) : JpaFragment<FragmentCustomerBalanceBinding>() {

    private var job: Job? = null

    private val viewModel: CustomerBalanceReportViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveData()
        setListener()
        getReport("")
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

        viewModel.customerBalanceReportLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setItemAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.editTextSearch.addTextChangedListener {
            binding.recyclerView.adapter = null
            job?.cancel()
            createJobForSearch(it.toString())
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- createJobForSearch
    private fun createJobForSearch(search: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(700)
            withContext(Dispatchers.Main) {
                getReport(search)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- createJobForSearch


    //---------------------------------------------------------------------------------------------- getReport
    private fun getReport(search: String) {
        binding.shimmerViewContainer.startLoading()
        viewModel.getCustomerBalanceReport(search)
    }
    //---------------------------------------------------------------------------------------------- getReport


    //---------------------------------------------------------------------------------------------- setItemAdapter
    private fun setItemAdapter(items: List<CustomerBalanceReportModel>) {
        if (context == null)
            return
        val click = object : CustomerBalanceHolder.Click {
            override fun reportDetail(item: CustomerBalanceReportModel) {
                val bundle = Bundle()
                bundle.putInt(CompanionValues.CUSTOMER_ID, item.customerId)
                bundle.putString(CompanionValues.CUSTOMER_NAME, item.customerName)
                bundle.putString(CompanionValues.CUSTOMER_CODE, item.customerCode)
                findNavController().navigate(
                    R.id.action_customerBalanceFragment_to_customerBalanceDetailFragment,
                    bundle
                )
            }
        }
        val adapter = CustomerBalanceAdapter(items, click)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setItemAdapter


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }
    //---------------------------------------------------------------------------------------------- onDestroyView

}