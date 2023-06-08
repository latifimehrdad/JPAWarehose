package com.zarholding.jpacustomer.view.fragment.report.billing_return

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnReportModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zar.core.view.picker.date.customviews.DateRangeCalendarView
import com.zar.core.view.picker.date.dialog.DatePickerDialog
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentReportBillingReturnBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.BillingReturnAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by m-latifi on 6/8/2023.
 */

@AndroidEntryPoint
class BillingReturnReportFragment(
    override var layout: Int = R.layout.fragment_report_billing_return
) : JpaFragment<FragmentReportBillingReturnBinding>() {

    private val viewModel: BillingReturnViewModel by viewModels()


    private enum class DateType {
        DateFrom,
        DateTo
    }

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        viewModel.setReportType(arguments)
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.dateFromLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            binding.textViewDateFrom.text = it
        }

        viewModel.dateToLiveData.observe(viewLifecycleOwner) {
            if (it != null)
                binding.shimmerViewContainer.startLoading()
            binding.textViewDateTo.text = it
        }

        viewModel.reportLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.textViewDateFrom.setOnClickListener { showDatePickerDialog(DateType.DateFrom) }

        binding.textViewDateTo.setOnClickListener {
            if (viewModel.dateFromLiveData.value.isNullOrEmpty())
                showMessage(getString(R.string.pleaseChooseDateFrom))
            else showDatePickerDialog(DateType.DateTo)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- showDatePickerDialog
    private fun showDatePickerDialog(dateType: DateType) {
        if (context == null)
            return
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.selectionMode = DateRangeCalendarView.SelectionMode.Single
        datePickerDialog.isDisableDaysAgo = false
        datePickerDialog.acceptButtonColor =
            resources.getColor(R.color.datePickerConfirmButtonBackColor, requireContext().theme)
        datePickerDialog.headerBackgroundColor =
            resources.getColor(R.color.datePickerConfirmButtonBackColor, requireContext().theme)
        datePickerDialog.headerTextColor =
            resources.getColor(R.color.white, requireContext().theme)
        datePickerDialog.weekColor =
            resources.getColor(R.color.textColorDisable, requireContext().theme)
        datePickerDialog.disableDateColor =
            resources.getColor(R.color.textColorDisable, requireContext().theme)
        datePickerDialog.defaultDateColor =
            resources.getColor(R.color.datePickerDateBackColor, requireContext().theme)
        datePickerDialog.selectedDateCircleColor =
            resources.getColor(R.color.datePickerConfirmButtonBackColor, requireContext().theme)
        datePickerDialog.selectedDateColor =
            resources.getColor(R.color.white, requireContext().theme)
        datePickerDialog.rangeDateColor =
            resources.getColor(R.color.datePickerConfirmButtonBackColor, requireContext().theme)
        datePickerDialog.rangeStripColor =
            resources.getColor(R.color.datePickerRangeColor, requireContext().theme)
        datePickerDialog.holidayColor =
            resources.getColor(R.color.red, requireContext().theme)
        datePickerDialog.textSizeWeek = 12.0f
        datePickerDialog.textSizeDate = 14.0f
        datePickerDialog.textSizeTitle = 18.0f
        datePickerDialog.setCanceledOnTouchOutside(true)
        datePickerDialog.onSingleDateSelectedListener =
            DatePickerDialog.OnSingleDateSelectedListener {
                when (dateType) {
                    DateType.DateFrom -> {
                        viewModel.setDateTo(null)
                        viewModel.setDateFrom(it.persianShortDate)
                    }

                    DateType.DateTo -> viewModel.setDateTo(it.persianShortDate)
                }
            }
        datePickerDialog.showDialog()

    }
    //---------------------------------------------------------------------------------------------- showDatePickerDialog


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<BillingAndReturnReportModel>) {
        if (context == null)
            return
        val adapter = BillingReturnAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewReport.adapter = adapter
        binding.recyclerViewReport.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setAdapter

}