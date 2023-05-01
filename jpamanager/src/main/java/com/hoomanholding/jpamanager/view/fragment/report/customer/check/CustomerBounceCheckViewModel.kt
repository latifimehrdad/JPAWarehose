package com.hoomanholding.jpamanager.view.fragment.report.customer.check

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.report.CustomerBounceCheckReportModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.model.repository.ReportRepository
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

    val itemLiveData: MutableLiveData<List<CustomerBounceCheckReportModel>> by lazy {
        MutableLiveData<List<CustomerBounceCheckReportModel>>()
    }


    //---------------------------------------------------------------------------------------------- requestCustomersBouncedCheckReport
    fun requestCustomersBouncedCheckReport() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val response = checkResponse(reportRepository.requestCustomersBouncedCheckReport())
            response?.let {
                itemLiveData.postValue(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestCustomersBouncedCheckReport

}