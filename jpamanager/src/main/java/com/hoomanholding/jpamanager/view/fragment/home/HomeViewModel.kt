package com.hoomanholding.jpamanager.view.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.model.data.other.HomeReportItemModel
import com.hoomanholding.jpamanager.model.repository.ReportRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


/**
 * create by m-latifi on 3/7/2023
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : JpaViewModel() {

    val homeReportLiveData: MutableLiveData<List<HomeReportItemModel>> by lazy {
        MutableLiveData<List<HomeReportItemModel>>()
    }


    //---------------------------------------------------------------------------------------------- requestFirstPageReport
    fun requestFirstPageReport() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val response = checkResponse(reportRepository.requestFirstPageReport())
            response?.let {
                val items = mutableListOf<HomeReportItemModel>()
                items.add(
                    HomeReportItemModel(
                        resourcesProvider.getString(R.string.invoice),
                        it.invoiceCount,
                        it.invoiceAmount
                    )
                )
                items.add(
                    HomeReportItemModel(
                        resourcesProvider.getString(R.string.returnInvoice),
                        it.returnInvoiceCount,
                        it.returnInvoiceAmount
                    )
                )
                items.add(
                    HomeReportItemModel(
                        resourcesProvider.getString(R.string.order),
                        it.orderCount,
                        it.orderAmount
                    )
                )
                items.add(
                    HomeReportItemModel(
                        resourcesProvider.getString(R.string.bouncedCheck),
                        it.bouncedCheckCount,
                        it.bouncedCheckAmount
                    )
                )
                homeReportLiveData.postValue(items)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestFirstPageReport

}