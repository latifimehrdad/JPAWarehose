package com.hoomanholding.jpamanager.view.fragment.report.customerbalance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.applibrary.model.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 4/29/2023
 */

@HiltViewModel
class CustomerBalanceReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
): JpaViewModel() {

    var customerBalanceReport: List<CustomerBalanceReportModel>? = null
    val customerBalanceReportLiveData: MutableLiveData<List<CustomerBalanceReportModel>> by lazy {
        MutableLiveData<List<CustomerBalanceReportModel>>()
    }

    //---------------------------------------------------------------------------------------------- getCustomerBalanceReport
    fun getCustomerBalanceReport(search: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            customerBalanceReport?.let {
                searchICustomerBalanceModel(search)
            } ?: run {
                callApi(
                    request = reportRepository.requestCustomerBalance(),
                    onReceiveData = {
                        customerBalanceReport = it
                        customerBalanceReportLiveData.postValue(it)
                    }
                )
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getCustomerBalanceReport



    //---------------------------------------------------------------------------------------------- searchICustomerBalanceModel
    private fun searchICustomerBalanceModel(search: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            customerBalanceReport?.let {list ->
                if (search.isNotEmpty()) {
                    val searchList = list.filter {
                        if (it.customerName?.contains(search) == true)
                            true
                        else if(it.customerCode?.contains(search) == true)
                            true
                        else it.visitorName?.contains(search) == true

                    }
                    customerBalanceReportLiveData.postValue(searchList)
                } else
                    customerBalanceReportLiveData.postValue(list)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- searchICustomerBalanceModel

}