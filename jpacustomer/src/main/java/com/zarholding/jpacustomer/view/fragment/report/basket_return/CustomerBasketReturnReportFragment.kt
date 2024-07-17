package com.zarholding.jpacustomer.view.fragment.report.basket_return


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.report.CustomerReturnBasketReportModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentReportBasketReturnBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.ReportCustomerBasketReturnAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomerBasketReturnReportFragment(
    override var layout: Int = R.layout.fragment_report_basket_return
) : JpaFragment<FragmentReportBasketReturnBinding>() {

    private val viewModel: CustomerBasketReturnReportViewModel by viewModels()


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopLoading()
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        setListener()
        binding.shimmerViewContainer.startLoading()
        viewModel.requestCustomerReturnBasketReport()
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

        viewModel.reportLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<CustomerReturnBasketReportModel>) {
        if (context == null)
            return
        val adapter = ReportCustomerBasketReturnAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewReport.layoutManager = manager
        binding.recyclerViewReport.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setAdapter

}