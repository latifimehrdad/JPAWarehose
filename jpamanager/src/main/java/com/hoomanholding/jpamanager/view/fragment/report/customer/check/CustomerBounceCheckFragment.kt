package com.hoomanholding.jpamanager.view.fragment.report.customer.check

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
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
import kotlinx.coroutines.*


/**
 * create by m-latifi on 5/1/2023
 */

@AndroidEntryPoint
class CustomerBounceCheckFragment(
    override var layout: Int = R.layout.fragment_customer_bounce_check
): JpaFragment<FragmentCustomerBounceCheckBinding>() {

    private var job: Job? = null
    private val viewModel : CustomerBounceCheckViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
        binding.viewModel = viewModel
        observeLiveData()
        setListener()
        if (viewModel.customerBounceReport == null)
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
    private fun getReport(search: String) {
        binding.shimmerViewContainer.startLoading()
        viewModel.requestCustomersBouncedCheckReport(search)
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
        binding.recyclerView.layoutManager = linearLayoutManager
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