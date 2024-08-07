package com.zarholding.jpacustomer.view.fragment.report.billing_return

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumReportType
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnReportModel
import com.hoomanholding.applibrary.model.repository.ReportRepository
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.viewmodel.JpaDownloadViewModel
import com.zarholding.jpacustomer.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 6/8/2023.
 */

@HiltViewModel
class BillingReturnViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : JpaDownloadViewModel() {


    private var reportType: EnumReportType = EnumReportType.Billing
    val dateFromLiveData: MutableLiveData<String?> by lazy { MutableLiveData<String?>() }
    val dateToLiveData: MutableLiveData<String?> by lazy { MutableLiveData<String?>() }
    val reportLiveData: MutableLiveData<List<BillingAndReturnReportModel>> by lazy {
        MutableLiveData<List<BillingAndReturnReportModel>>()
    }

    //---------------------------------------------------------------------------------------------- getReportType
    fun getReportType() = reportType
    //---------------------------------------------------------------------------------------------- getReportType


    //---------------------------------------------------------------------------------------------- getReportTypeString
    fun getReportTypeString() = reportType.name
    //---------------------------------------------------------------------------------------------- getReportTypeString


    //---------------------------------------------------------------------------------------------- setDateFrom
    fun setDateFrom(date: String?) {
        dateFromLiveData.postValue(date)
    }
    //---------------------------------------------------------------------------------------------- setDateFrom


    //---------------------------------------------------------------------------------------------- setDateTo
    fun setDateTo(date: String?) {
        dateToLiveData.postValue(date)
        if (date != null)
            getReport()
    }
    //---------------------------------------------------------------------------------------------- setDateTo


    //---------------------------------------------------------------------------------------------- setReportType
    fun setReportType(bundle: Bundle?) {
        bundle?.let {
            val type = it.getString(CompanionValues.REPORT_TYPE, EnumReportType.Billing.name)
            reportType = enumValueOf(type)
        }
    }
    //---------------------------------------------------------------------------------------------- setReportType


    //---------------------------------------------------------------------------------------------- getReport
    private fun getReport() {
        viewModelScope.launch(IO + exceptionHandler()) {
            delay(1000)
            if (dateFromLiveData.value == null) {
                setMessage(resourcesProvider.getString(R.string.pleaseChooseDateFrom))
                return@launch
            }
            if (dateToLiveData.value == null) {
                setMessage(resourcesProvider.getString(R.string.pleaseChooseDateTo))
                return@launch
            }

            when(reportType){
                EnumReportType.Billing -> callApi(
                    request = reportRepository
                        .requestCustomerBillingReport(
                            dateFromLiveData.value!!,
                            dateToLiveData.value!!
                        ),
                    onReceiveData = {
                        reportLiveData.postValue(it)
                    }
                )
                EnumReportType.Return -> callApi(
                    request = reportRepository
                        .requestCustomerReturnReport(
                            dateFromLiveData.value!!,
                            dateToLiveData.value!!
                        ),
                    onReceiveData = {
                        reportLiveData.postValue(it)
                    }
                )
                else -> {}
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getReport


    //---------------------------------------------------------------------------------------------- downloadCustomersBillingReturnPDF
    fun downloadCustomersBillingReturnPDF(billingId: Long, type: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            val file = initFile(type, "pdf")
            delay(1000)
            val response = downloadFileRepository.downloadCustomersBillingPDF(billingId, type)
            response.body()?.saveFile(file)?.collect { downloadState ->
                when (downloadState) {
                    is DownloadState.Downloading -> {
                        downloadProgress.postValue(downloadState.progress)
                    }

                    is DownloadState.Failed -> {
                        setMessage(downloadState.error?.message ?: "Failed Download!")
                    }

                    DownloadState.Finished -> {
                        downloadSuccessLiveData.postValue(file)
                    }
                }
            } ?: run {
                setMessage(response.errorBody()?.string() ?: "")
            }
        }
    }
    //---------------------------------------------------------------------------------------------- downloadCustomersBillingReturnPDF


    //---------------------------------------------------------------------------------------------- requestCustomerHeaderBillingsPDF
    fun requestCustomerHeaderBillingsPDF() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val file = initFile(getReportType().name, "pdf")
            delay(1000)
            val response = downloadFileRepository
                .requestCustomerHeaderBillingsPDF(
                    dateFromLiveData.value!!,
                    dateToLiveData.value!!,
                    getReportType().name
                )
            response.body()?.saveFile(file)?.collect { downloadState ->
                when (downloadState) {
                    is DownloadState.Downloading -> {
                        downloadProgress.postValue(downloadState.progress)
                    }

                    is DownloadState.Failed -> {
                        setMessage(downloadState.error?.message ?: "Failed Download!")
                    }

                    DownloadState.Finished -> {
                        downloadSuccessLiveData.postValue(file)
                    }
                }
            } ?: run {
                setMessage(response.errorBody()?.string() ?: "")
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestCustomerHeaderBillingsPDF


}