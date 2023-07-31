package com.hoomanholding.jpamanager.view.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.currency.CurrencyModel
import com.hoomanholding.applibrary.model.data.response.report.HomeReportModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.model.data.other.HomeReportItemModel
import com.hoomanholding.jpamanager.model.repository.CurrencyRepository
import com.hoomanholding.applibrary.model.repository.ReportRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


/**
 * create by m-latifi on 3/7/2023
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val currencyRepository: CurrencyRepository
) : JpaViewModel() {

    var currencyTypeText: String = ""

    val homeReportLiveData: MutableLiveData<List<HomeReportItemModel>> by lazy {
        MutableLiveData<List<HomeReportItemModel>>()
    }

    val currencyLiveData: MutableLiveData<List<CurrencyModel>> by lazy {
        MutableLiveData<List<CurrencyModel>>()
    }


    //---------------------------------------------------------------------------------------------- requestGetCurrency
    fun requestGetCurrency() {
        if (currencyLiveData.value != null)
            return
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = currencyRepository.requestGetCurrency(),
                onReceiveData = { currencyLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCurrency


    //---------------------------------------------------------------------------------------------- requestFirstPageReport
    fun requestFirstPageReport(currencyTypePosition: Int) {
        viewModelScope.launch(IO + exceptionHandler()) {
            currencyTypeText = currencyLiveData.value?.get(currencyTypePosition)?.value ?: ""
            val id = currencyLiveData.value?.get(currencyTypePosition)?.id
            if (id != null) {
                callApi(
                    request = reportRepository.requestFirstPageReport(id),
                    onReceiveData = {
                        setFirstPageReport(it)
                    }
                )
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestFirstPageReport


    //---------------------------------------------------------------------------------------------- setFirstPageReport
    private fun setFirstPageReport(homeReportModel: HomeReportModel) {
        val items = mutableListOf<HomeReportItemModel>()
        items.add(
            HomeReportItemModel(
                resourcesProvider.getString(R.string.totalSale),
                homeReportModel.invoiceCount,
                homeReportModel.invoiceAmount
            )
        )
        items.add(
            HomeReportItemModel(
                resourcesProvider.getString(R.string.pluralReturn),
                homeReportModel.returnInvoiceCount,
                homeReportModel.returnInvoiceAmount
            )
        )
        items.add(
            HomeReportItemModel(
                resourcesProvider.getString(R.string.pluralOrder),
                homeReportModel.orderCount,
                homeReportModel.orderAmount
            )
        )
        items.add(
            HomeReportItemModel(
                resourcesProvider.getString(R.string.bouncedCheck),
                homeReportModel.bouncedCheckCount,
                homeReportModel.bouncedCheckAmount
            )
        )
        items.add(
            HomeReportItemModel(
                resourcesProvider.getString(R.string.wareHouseStocks),
                homeReportModel.wareHouseStocks,
                homeReportModel.wareHouseStocksAmount
            )
        )
        homeReportLiveData.postValue(items)
    }
    //---------------------------------------------------------------------------------------------- setFirstPageReport

}