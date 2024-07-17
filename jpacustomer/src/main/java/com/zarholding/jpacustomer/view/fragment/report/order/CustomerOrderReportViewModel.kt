package com.zarholding.jpacustomer.view.fragment.report.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderModel
import com.hoomanholding.applibrary.model.repository.ReportRepository
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.viewmodel.JpaDownloadViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 6/7/2023.
 */

@HiltViewModel
class CustomerOrderReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val tokenRepository: TokenRepository
) : JpaDownloadViewModel() {


    val reportLiveData: MutableLiveData<List<ReportCustomerOrderModel>> by lazy {
        MutableLiveData<List<ReportCustomerOrderModel>>()
    }


    //---------------------------------------------------------------------------------------------- requestCustomerOrderReport
    fun requestCustomerOrderReport() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            callApi(
                request = reportRepository.requestCustomerOrderReport(),
                onReceiveData = { reportLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestCustomerOrderReport


    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken

}