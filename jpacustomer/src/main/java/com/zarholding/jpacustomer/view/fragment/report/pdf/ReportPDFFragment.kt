package com.zarholding.jpacustomer.view.fragment.report.pdf

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.enums.EnumReportType
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnReportModel
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentReportPdfBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.BalancePDFAdapter
import com.zarholding.jpacustomer.view.adapter.recycler.BillingReturnPDFAdapter
import com.zarholding.jpacustomer.view.fragment.report.balance.CustomerBalanceReportViewModel
import com.zarholding.jpacustomer.view.fragment.report.billing_return.BillingReturnViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created by m-latifi on 6/7/2023.
 */

@AndroidEntryPoint
class ReportPDFFragment(
    override var layout: Int = R.layout.fragment_report_pdf
) : JpaFragment<FragmentReportPdfBinding>() {

    private val viewModel: ReportPDFViewModel by viewModels()
    private val viewModelBalance: CustomerBalanceReportViewModel by viewModels()
    private val viewModelBillingReturn: BillingReturnViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        getReport()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopLoading()
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            if (it)
                showMessage("فایل در پوشه Documents ذخیره شد")
            else
                showMessage("خطا در تولید فایل")

            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        viewModelBalance.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModelBillingReturn.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModelBillingReturn.reportLiveData.observe(viewLifecycleOwner) {
            setBillingReturnAdapter(it)
        }

        viewModelBalance.reportDetailLiveData.observe(viewLifecycleOwner) {
            setBalanceAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate



    //---------------------------------------------------------------------------------------------- getReport
    private fun getReport() {
        arguments?.let {
            binding.shimmerViewContainer.startLoading()
            val type = it.getString(CompanionValues.REPORT_TYPE, EnumReportType.Billing.name)
            when (val reportType = enumValueOf<EnumReportType>(type)) {
                EnumReportType.Return -> {
                    binding.textViewTitle.text = getString(R.string.feedbackReport)
                    getBillingReport(it, reportType)
                }

                EnumReportType.Billing -> {
                    binding.textViewTitle.text = getString(R.string.factorReport)
                    getBillingReport(it, reportType)

                }

                EnumReportType.Balance -> {
                    binding.textViewTitle.text = getString(R.string.customerBalanceReport)
                    viewModelBalance.getUserInfo()
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getReport


    //---------------------------------------------------------------------------------------------- getBillingReport
    private fun getBillingReport(bundle: Bundle, type: EnumReportType) {
        viewModelBillingReturn.setReportType(type)
        val startDate = bundle.getString(CompanionValues.REPORT_START_DATE)
        val endDate = bundle.getString(CompanionValues.REPORT_END_DATE)
        binding.textViewDescription.text = getString(R.string.reportFromTo, startDate, endDate)
        viewModelBillingReturn.setDateFrom(startDate)
        viewModelBillingReturn.setDateTo(endDate)
    }
    //---------------------------------------------------------------------------------------------- getBillingReport



    //---------------------------------------------------------------------------------------------- setBalanceAdapter
    private fun setBalanceAdapter(items: List<CustomerBalanceReportDetailModel>) {
        binding.constraintLayoutBillingReturnTitle.visibility = View.GONE
        binding.constraintLayoutBalanceTitle.visibility = View.VISIBLE
        if (items.isNotEmpty()) {
            val item = items.last()
            binding.textViewTotal.setTitleAndValue(
                title = getString(R.string.totalDebit),
                splitter = getString(R.string.colon),
                last = getString(R.string.rial),
                value = item.balance
            )
        }
        val adapter = BalancePDFAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewReport.adapter = adapter
        binding.recyclerViewReport.layoutManager = manager
        CoroutineScope(Main).launch {
            delay(500)
            viewModel.createPdf(binding.linearLayoutParent)
        }
    }
    //---------------------------------------------------------------------------------------------- setBalanceAdapter


    //---------------------------------------------------------------------------------------------- setBillingReturnAdapter
    private fun setBillingReturnAdapter(items: List<BillingAndReturnReportModel>) {
        binding.constraintLayoutBillingReturnTitle.visibility = View.VISIBLE
        binding.constraintLayoutBalanceTitle.visibility = View.GONE
        binding.textViewTotal.visibility = View.GONE
        val adapter = BillingReturnPDFAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewReport.adapter = adapter
        binding.recyclerViewReport.layoutManager = manager
        CoroutineScope(Main).launch {
            delay(500)
            withContext(IO) {
                viewModel.createPdf(binding.linearLayoutParent)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setBillingReturnAdapter


}