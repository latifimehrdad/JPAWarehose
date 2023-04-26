package com.hoomanholding.jpamanager.view.fragment.report.sale

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentReportVisitorSaleBinding
import com.hoomanholding.jpamanager.tools.chart.VisitorCustomerCombineChart
import com.hoomanholding.jpamanager.tools.chart.VisitorSaleCombineChart
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.StringSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 4/25/2023
 */

@AndroidEntryPoint
class VisitorSaleReportFragment(override var layout: Int = R.layout.fragment_report_visitor_sale) :
    JpaFragment<FragmentReportVisitorSaleBinding>() {

    private val viewModel: VisitorSaleReportViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        initView()
//        setChart()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- initView
    private fun initView(){
        val items = listOf("نمودار میزان فروش","نمودار تعداد فروش")
        binding.spinnerChooseChart.apply {
            setSpinnerAdapter(StringSpinnerAdapter(this))
            setItems(items)
            getSpinnerRecyclerView().layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setOnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
                if (newIndex == 0)
                    setCombinedChartSale()
                else if (newIndex == 1)
                    setCombinedChartCustomer()
            }
        }
        viewModel.requestVisitorSalesReport()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
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

        viewModel.visitorSaleReportLiveData.observe(viewLifecycleOwner){
            binding.spinnerChooseChart.selectItemByIndex(0)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- setCombinedChartSale
    private fun setCombinedChartSale() {
        binding.combinedChartCustomer.visibility = View.GONE
        viewModel.visitorSaleReportLiveData.value?.let {
            VisitorSaleCombineChart(binding.combinedChartSale, it)
        }
    }
    //---------------------------------------------------------------------------------------------- setCombinedChartSale


    //---------------------------------------------------------------------------------------------- setCombinedChartCustomer
    private fun setCombinedChartCustomer() {
        binding.combinedChartSale.visibility = View.GONE
        viewModel.visitorSaleReportLiveData.value?.let {
            VisitorCustomerCombineChart(binding.combinedChartCustomer, it)
        }
    }
    //---------------------------------------------------------------------------------------------- setCombinedChartCustomer



}