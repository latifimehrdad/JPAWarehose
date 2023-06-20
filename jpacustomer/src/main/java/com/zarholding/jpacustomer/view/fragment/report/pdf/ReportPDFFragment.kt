package com.zarholding.jpacustomer.view.fragment.report.pdf

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.enums.EnumReportType
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderDetailModel
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
import com.zarholding.jpacustomer.view.adapter.recycler.BillingReturnDetailPDFAdapter
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
            activity?.onBackPressedDispatcher?.onBackPressed()
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModelBillingReturn.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            activity?.onBackPressedDispatcher?.onBackPressed()
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

        viewModelBillingReturn.reportDetailLiveData.observe(viewLifecycleOwner){
            setBillingReturnDetail(it)
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
                    getBillingReturnReport(it, reportType)
                }

                EnumReportType.Billing -> {
                    binding.textViewTitle.text = getString(R.string.factorReport)
                    getBillingReturnReport(it, reportType)

                }

                EnumReportType.Balance -> {
                    binding.textViewTitle.text = getString(R.string.customerBalanceReport)
                    viewModelBalance.getUserInfo()
                }

                EnumReportType.BillingItem -> {
                    getBillingReturnDetail(it, EnumReportType.Billing.name)
                }

                EnumReportType.ReturnItem -> {
                    getBillingReturnDetail(it, EnumReportType.Return.name)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getReport


    //---------------------------------------------------------------------------------------------- getBillingReturnReport
    private fun getBillingReturnReport(bundle: Bundle, type: EnumReportType) {
        viewModelBillingReturn.setReportType(type)
        val startDate = bundle.getString(CompanionValues.REPORT_START_DATE)
        val endDate = bundle.getString(CompanionValues.REPORT_END_DATE)
        binding.textViewDescription.text = getString(R.string.reportFromTo, startDate, endDate)
        viewModelBillingReturn.setDateFrom(startDate)
        viewModelBillingReturn.setDateTo(endDate)
    }
    //---------------------------------------------------------------------------------------------- getBillingReturnReport


    //---------------------------------------------------------------------------------------------- getBillingReturnDetail
    private fun getBillingReturnDetail(bundle: Bundle, type: String) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        binding.textViewTitle.text = getString(R.string.billOfSale)
        val billingId = bundle.getLong(CompanionValues.ORDER_ID)
        viewModelBillingReturn.getBillingReturnDetail(billingId, type)
    }
    //---------------------------------------------------------------------------------------------- getBillingReturnDetail


    //---------------------------------------------------------------------------------------------- setBalanceAdapter
    private fun setBalanceAdapter(items: List<CustomerBalanceReportDetailModel>) {
        binding.layoutBalanceTitle.root.visibility = View.VISIBLE
        binding.textViewTotal.visibility = View.VISIBLE
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
        binding.layoutBillReturnTitle.root.visibility = View.VISIBLE
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



    //---------------------------------------------------------------------------------------------- setBillingReturnDetail
    private fun setBillingReturnDetail(item: CustomerOrderDetailModel){
        binding.layoutBillReturnDetailTitle.root.visibility = View.VISIBLE
        binding.constraintLayoutTotalAmount.visibility = View.VISIBLE
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        binding.layoutBillReturnDetailTitle.textViewDetailDate.setTitleAndValue(
            title = getString(R.string.orderDate),
            splitter = getString(R.string.colon),
            value = item.billingDate
        )
        binding.layoutBillReturnDetailTitle.textViewDetailNumber.setTitleAndValue(
            title = getString(R.string.orderNumber),
            splitter = getString(R.string.colon),
            value = item.billingNumber
        )
        binding.layoutBillReturnDetailTitle.textViewVisitor.setTitleAndValue(
            title = getString(R.string.visitorName),
            splitter = getString(R.string.colon),
            value = item.visitorName
        )
        binding.layoutBillReturnDetailTitle.textViewSellerAddress.setTitleAndValue(
            title = getString(R.string.address),
            splitter = getString(R.string.colon),
            value = item.salerAddress
        )
        binding.layoutBillReturnDetailTitle.textViewTel.setTitleAndValue(
            title = getString(R.string.phoneNumber),
            splitter = getString(R.string.colon),
            value = item.customerPhone
        )
        binding.layoutBillReturnDetailTitle.textViewBuyerName.setTitleAndValue(
            title = getString(R.string.buyerName),
            splitter = getString(R.string.colon),
            value = item.customerName
        )
        binding.layoutBillReturnDetailTitle.textViewBuyerMobile.setTitleAndValue(
            title = getString(R.string.mobileNumber),
            splitter = getString(R.string.colon),
            value = item.customerMobile
        )
        binding.layoutBillReturnDetailTitle.textViewStoreName.setTitleAndValue(
            title = getString(R.string.shopName),
            splitter = getString(R.string.colon),
            value = item.storeName
        )
        binding.layoutBillReturnDetailTitle.textViewBuyerCode.setTitleAndValue(
            title = getString(R.string.customerCode),
            splitter = getString(R.string.colon),
            value = item.customerCode
        )
        binding.layoutBillReturnDetailTitle.textViewBuyerAddress.setTitleAndValue(
            title = getString(R.string.shopAddress),
            splitter = getString(R.string.colon),
            value = item.customerAddress
        )
        binding.textViewTotalPrice.setTitleAndValue(
            title = getString(R.string.totalAmount),
            splitter = getString(R.string.colon),
            last = getString(R.string.rial),
            value = item.amount
        )
        binding.textViewDiscount.setTitleAndValue(
            title = getString(R.string.discount),
            splitter = getString(R.string.colon),
            last = getString(R.string.rial),
            value = item.discount
        )
        binding.textViewFinalAmount.setTitleAndValue(
            title = getString(R.string.finalPrice),
            splitter = getString(R.string.colon),
            last = getString(R.string.rial),
            value = item.finalAmount
        )

        item.items?.let {
            val adapter = BillingReturnDetailPDFAdapter(it)
            binding.recyclerViewReport.adapter = adapter
            binding.recyclerViewReport.layoutManager = manager
            CoroutineScope(Main).launch {
                delay(500)
                withContext(IO) {
                    viewModel.createPdf(binding.linearLayoutParent)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setBillingReturnDetail


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.destinationFile?.let {
            val fileURI = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + ".provider",
                it
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(fileURI, "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            requireContext().startActivity(intent)
        }
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
    //---------------------------------------------------------------------------------------------- onDestroyView

}