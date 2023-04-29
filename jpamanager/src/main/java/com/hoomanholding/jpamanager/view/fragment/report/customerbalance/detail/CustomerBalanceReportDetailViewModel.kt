package com.hoomanholding.jpamanager.view.fragment.report.customerbalance.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.report.CustomerBalanceReportDetailModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.model.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 4/29/2023
 */

@HiltViewModel
class CustomerBalanceReportDetailViewModel @Inject constructor(
    private val reportRepository: ReportRepository
): JpaViewModel() {

    val customerCodeLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val customerNameLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val reportDetailLiveData: MutableLiveData<List<CustomerBalanceReportDetailModel>> by lazy {
        MutableLiveData<List<CustomerBalanceReportDetailModel>>()
    }



    //---------------------------------------------------------------------------------------------- setValueFromArgument
    fun setValueFromArgument(bundle: Bundle){
        val name = bundle.getString(CompanionValues.CUSTOMER_NAME)
        val code = bundle.getString(CompanionValues.CUSTOMER_CODE)
        val id = bundle.getInt(CompanionValues.CUSTOMER_ID)
        customerCodeLiveData.postValue(code)
        customerNameLiveData.postValue(name)
        requestCustomerBalanceDetail(id)
    }
    //---------------------------------------------------------------------------------------------- setValueFromArgument


    //---------------------------------------------------------------------------------------------- requestCustomerBalanceDetail
    private fun requestCustomerBalanceDetail(customerId : Int){
        viewModelScope.launch(IO + exceptionHandler()) {
            val response = checkResponse(reportRepository.requestCustomerBalanceDetail(customerId))
            response?.let {
                reportDetailLiveData.postValue(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestCustomerBalanceDetail

}