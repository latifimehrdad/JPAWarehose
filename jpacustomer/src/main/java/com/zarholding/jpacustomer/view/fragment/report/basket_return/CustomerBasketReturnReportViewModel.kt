package com.zarholding.jpacustomer.view.fragment.report.basket_return

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.report.CustomerReturnBasketReportModel
import com.hoomanholding.applibrary.model.repository.ReportRepository
import com.hoomanholding.applibrary.viewmodel.JpaDownloadViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CustomerBasketReturnReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : JpaDownloadViewModel() {


    val reportLiveData: MutableLiveData<List<CustomerReturnBasketReportModel>> by lazy {
        MutableLiveData<List<CustomerReturnBasketReportModel>>()
    }


    //---------------------------------------------------------------------------------------------- requestCustomerReturnBasketReport
    fun requestCustomerReturnBasketReport() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            callApi(
                request = reportRepository.requestCustomerReturnBasketReport(),
                onReceiveData = { reportLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestCustomerReturnBasketReport

}