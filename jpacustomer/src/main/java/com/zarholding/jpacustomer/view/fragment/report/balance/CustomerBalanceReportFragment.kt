package com.zarholding.jpacustomer.view.fragment.report.balance

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.applibrary.tools.PermissionManager
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentReportBalanceBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.CustomerBalanceAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by m-latifi on 6/7/2023.
 */


@AndroidEntryPoint
class CustomerBalanceReportFragment(
    override var layout: Int = R.layout.fragment_report_balance
) : JpaFragment<FragmentReportBalanceBinding>() {

    private val viewModel: CustomerBalanceReportViewModel by viewModels()

    @Inject
    lateinit var permissionManager: PermissionManager

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
        viewModel.getReport()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            binding.textViewReport.text = getString(R.string.createPDF)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }


        viewModel.reportDetailLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }

        viewModel.downloadProgress.observe(viewLifecycleOwner) {
            val title = "${getString(R.string.createPDF)} - $it %"
            binding.textViewReport.text = title
        }

        viewModel.downloadSuccessLiveData.observe(viewLifecycleOwner) {
            binding.textViewReport.text = getString(R.string.createPDF)
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
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.textViewReport.setOnClickListener {
            permissionForPdf()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<CustomerBalanceReportDetailModel>) {
        if (context == null)
            return
        val adapter = CustomerBalanceAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewReport.layoutManager = manager
        binding.recyclerViewReport.adapter = adapter
        if (items.isNotEmpty()) {
            val item = items.last()
            if (item.balance > 0) {
                binding.textViewTotal.setTextColor(requireContext().getColor(R.color.red))
                binding.textViewTotal.setTitleAndValue(
                    title = getString(R.string.totalDebit),
                    splitter = getString(R.string.colon),
                    last = getString(R.string.rial),
                    value = item.balance
                )
            } else {
                binding.textViewTotal.setTextColor(requireContext().getColor(R.color.green))
                binding.textViewTotal.setTitleAndValue(
                    title = getString(R.string.totalCredit),
                    splitter = getString(R.string.colon),
                    last = getString(R.string.rial),
                    value = abs(item.balance)
                )
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setAdapter

    //---------------------------------------------------------------------------------------------- storagePermissionLauncher
    private val storagePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            permissionManager.checkPermissionResult(results) {
                if (it)
                    downloadCustomerBalancePDF()
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
                downloadCustomerBalancePDF()
        } else downloadCustomerBalancePDF()
    }
    //______________________________________________________________________________________________ permissionForPdf


    //---------------------------------------------------------------------------------------------- downloadCustomerBalancePDF
    private fun downloadCustomerBalancePDF() {
        binding.textViewReport.text = getString(R.string.bePatient)
        viewModel.downloadCustomerBalancePDF()
    }
    //---------------------------------------------------------------------------------------------- downloadCustomerBalancePDF
}