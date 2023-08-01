package com.zarholding.jpacustomer.view.fragment.report.balance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumReportType
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.applibrary.model.repository.ReportRepository
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.viewmodel.JpaDownloadViewModel
import com.zar.core.tools.api.checkResponseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 6/7/2023.
 */

@HiltViewModel
class CustomerBalanceReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : JpaDownloadViewModel() {


    val reportDetailLiveData: MutableLiveData<List<CustomerBalanceReportDetailModel>> by lazy {
        MutableLiveData<List<CustomerBalanceReportDetailModel>>()
    }


    //---------------------------------------------------------------------------------------------- getReport
    fun getReport() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            val userInfo = userRepository.getUser()
            userInfo?.let {
                requestCustomerBalanceDetail(userInfo.id)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getReport


    //---------------------------------------------------------------------------------------------- requestCustomerBalanceDetail
    private fun requestCustomerBalanceDetail(customerId: Long) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            callApi(
                request = reportRepository.requestCustomerBalanceDetail(customerId),
                onReceiveData = { reportDetailLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestCustomerBalanceDetail


    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken


    //---------------------------------------------------------------------------------------------- downloadCustomerBalancePDF
    fun downloadCustomerBalancePDF() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            val file = initFile(EnumReportType.Balance.name, "pdf")
            delay(1000)
            val response = downloadFileRepository.downloadCustomerBalancePDF()
            if (response.code() == 200)
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
                    checkResponseError(response, errorLiveDate)
                }
            else
                checkResponseError(response, errorLiveDate)
        }
    }
    //---------------------------------------------------------------------------------------------- downloadCustomerBalancePDF


}