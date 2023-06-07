package com.hoomanholding.jpamanager.view.fragment.report.sale

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.report.VisitorSalesReportModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.applibrary.model.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 4/25/2023
 */

@HiltViewModel
class VisitorSaleReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
): JpaViewModel() {

    val visitorSaleReportLiveData: MutableLiveData<List<VisitorSalesReportModel>> by lazy {
        MutableLiveData<List<VisitorSalesReportModel>>()
    }


    //---------------------------------------------------------------------------------------------- requestVisitorSalesReport
    fun requestVisitorSalesReport() {
        viewModelScope.launch(IO + exceptionHandler()){
            val response = checkResponse(reportRepository.requestVisitorSalesReport())
            response?.let {
                visitorSaleReportLiveData.postValue(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestVisitorSalesReport

}