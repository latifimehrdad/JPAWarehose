package com.hoomanholding.jpamanager.view.fragment.report.customer.check

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.report.CustomerBounceCheckReportModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.applibrary.model.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 5/1/2023
 */

@HiltViewModel
class CustomerBounceCheckViewModel @Inject constructor(
    private val reportRepository: ReportRepository
): JpaViewModel(){

    var customerBounceReport: List<CustomerBounceCheckReportModel>? = null
    val itemLiveData: MutableLiveData<List<CustomerBounceCheckReportModel>> by lazy {
        MutableLiveData<List<CustomerBounceCheckReportModel>>()
    }


    //---------------------------------------------------------------------------------------------- requestCustomersBouncedCheckReport
    fun requestCustomersBouncedCheckReport(search: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            customerBounceReport?.let {
                searchICustomerBounce(search)
            } ?: run {
                val response = checkResponse(reportRepository.requestCustomersBouncedCheckReport())
                response?.let {
                    customerBounceReport = it
                    itemLiveData.postValue(it)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestCustomersBouncedCheckReport


    //---------------------------------------------------------------------------------------------- searchICustomerBounce
    private fun searchICustomerBounce(search: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            customerBounceReport?.let {list ->
                if (search.isNotEmpty()) {
                    val searchList = list.filter {
                        if (it.customerName?.contains(search) == true)
                            true
                        else if (it.customerCode?.contains(search) == true)
                            true
                        else if(it.checkNumber?.contains(search) == true)
                            true
                        else it.storeName?.contains(search) == true
                    }
                    itemLiveData.postValue(searchList)
                } else
                    itemLiveData.postValue(list)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- searchICustomerBounce

}