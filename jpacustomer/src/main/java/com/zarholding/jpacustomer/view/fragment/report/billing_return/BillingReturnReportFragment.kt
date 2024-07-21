package com.zarholding.jpacustomer.view.fragment.report.billing_return

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.enums.EnumReportType
import com.hoomanholding.applibrary.model.data.response.customer.CustomersModel
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnReportModel
import com.hoomanholding.applibrary.tools.PermissionManager
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zar.core.view.picker.date.customviews.DateRangeCalendarView
import com.zar.core.view.picker.date.dialog.DatePickerDialog
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentReportBillingReturnBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.holder.BillingReturnHolder
import com.zarholding.jpacustomer.view.adapter.recycler.BillingReturnAdapter
import com.zarholding.jpacustomer.view.adapter.recycler.CustomersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 6/8/2023.
 */

@AndroidEntryPoint
class BillingReturnReportFragment(
    override var layout: Int = R.layout.fragment_report_billing_return
) : JpaFragment<FragmentReportBillingReturnBinding>() {

    private val viewModel: BillingReturnViewModel by viewModels()

    @Inject
    lateinit var permissionManager: PermissionManager

    private var textViewListPdf: TextView? = null
    private var isEnableForSearching: Boolean = true


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
        binding.shimmerViewContainer.stopLoading()
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.constraintLayoutCustomer.visibility = View.GONE
        binding.recyclerViewCustomer.visibility = View.GONE
        viewModel.setReportType(arguments)
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        setListener()
        if (viewModel.isCustomerCustomers()) {
            binding.constraintLayoutCustomer.visibility = View.VISIBLE
        }
        when(viewModel.getReportType()) {
            EnumReportType.Billing -> binding.textViewTitle.text = getString(R.string.factorReport)
            EnumReportType.Return -> binding.textViewTitle.text = getString(R.string.feedbackReport)
            EnumReportType.Balance -> binding.textViewTitle.text = getString(R.string.customerBalanceReport)
        }
        viewModel.requestGetCustomers()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            textViewListPdf?.text = getString(R.string.createPDF)
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.dateFromLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            binding.recyclerViewReport.adapter = null
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

        viewModel.downloadProgress.observe(viewLifecycleOwner) {
            val title = "${getString(R.string.createPDF)} - $it %"
            textViewListPdf?.text = title
        }

        viewModel.downloadSuccessLiveData.observe(viewLifecycleOwner) {
            textViewListPdf?.text = getString(R.string.createPDF)
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

        viewModel.customersLiveData.observe(viewLifecycleOwner) {
            setCustomerAdapter(it)
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

        binding.textViewReport.setOnClickListener {
            permissionForPdf()
        }

        binding.imageViewClearText.setOnClickListener {
            binding.editTextSearch.setText("")
        }

        binding.editTextSearch.addTextChangedListener {
            if (isEnableForSearching) {
                val text = it.toString()
                if (text.isEmpty())
                    binding.imageViewClearText.visibility = View.GONE
                else
                    binding.imageViewClearText.visibility = View.VISIBLE
                viewModel.filterCustomer(customer = text)
            }
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
            resources.getColor(R.color.a_textHint, requireContext().theme)
        datePickerDialog.disableDateColor =
            resources.getColor(R.color.a_textHint, requireContext().theme)
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
        val click = object : BillingReturnHolder.Click {
            override fun detailPdf(id: Long, textView: TextView) {
                textViewListPdf = textView

                when (viewModel.getReportType()) {
                    EnumReportType.Return -> {
                        textView.text = getText(R.string.bePatient)
                        viewModel.downloadCustomersBillingReturnPDF(id, EnumReportType.Return.name)
                    }

                    EnumReportType.Billing -> {
                        textView.text = getText(R.string.bePatient)
                        viewModel.downloadCustomersBillingReturnPDF(id, EnumReportType.Billing.name)
                    }

                    else -> {}
                }
            }
        }
        val adapter = BillingReturnAdapter(items, click)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewReport.adapter = adapter
        binding.recyclerViewReport.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setAdapter


    //---------------------------------------------------------------------------------------------- setCustomerAdapter
    private fun setCustomerAdapter(items: List<CustomersModel>) {
        if (context == null)
            return
        val adapter = CustomersAdapter(items = items) {
            selectCustomer(customersModel = it)
        }
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewCustomer.layoutManager = manager
        binding.recyclerViewCustomer.adapter = adapter
        binding.recyclerViewCustomer.visibility = View.VISIBLE
    }
    //---------------------------------------------------------------------------------------------- setCustomerAdapter


    //---------------------------------------------------------------------------------------------- storagePermissionLauncher
    private val storagePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            permissionManager.checkPermissionResult(results) {
                if (it)
                    downloadCustomerPDF()
            }
        }
    //---------------------------------------------------------------------------------------------- storagePermissionLauncher


    //______________________________________________________________________________________________ permissionForPdf
    private fun permissionForPdf() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            val permissions = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val check = permissionManager.isPermissionGranted(
                permissions,
                storagePermissionLauncher
            )
            if (check)
                downloadCustomerPDF()
        } else downloadCustomerPDF()
    }
    //______________________________________________________________________________________________ permissionForPdf


    //---------------------------------------------------------------------------------------------- downloadCustomerPDF
    private fun downloadCustomerPDF() {
        textViewListPdf = binding.textViewReport
        binding.textViewReport.text = getString(R.string.bePatient)
        viewModel.requestCustomerHeaderBillingsPDF()
    }
    //---------------------------------------------------------------------------------------------- downloadCustomerPDF


    //---------------------------------------------------------------------------------------------- selectCustomer
    private fun selectCustomer(customersModel: CustomersModel) {
        binding.recyclerViewCustomer.visibility = View.GONE
        viewModel.setCustomer(customersModel = customersModel)
        binding.imageViewClearText.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            isEnableForSearching = false
            delay(500)
            binding.editTextSearch.setText(customersModel.customerName)
            delay(500)
            isEnableForSearching = true
        }
    }
    //---------------------------------------------------------------------------------------------- selectCustomer
}