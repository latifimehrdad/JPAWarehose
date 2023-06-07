package com.hoomanholding.jpamanager.view.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.currency.CurrencyModel
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
    fun requestGetCurrency(){
        if (currencyLiveData.value != null)
            return
        viewModelScope.launch(IO + exceptionHandler()) {
            val response = checkResponse(currencyRepository.requestGetCurrency())
            response?.let {
                currencyLiveData.postValue(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCurrency



    //---------------------------------------------------------------------------------------------- requestFirstPageReport
    fun requestFirstPageReport(currencyTypePosition: Int) {
        viewModelScope.launch(IO + exceptionHandler()) {
            currencyTypeText = currencyLiveData.value?.get(currencyTypePosition)?.value ?: ""
            val id = currencyLiveData.value?.get(currencyTypePosition)?.id
            if (id != null) {
                val response =
                    checkResponse(reportRepository.requestFirstPageReport(id))
                response?.let {
                    val items = mutableListOf<HomeReportItemModel>()
                    items.add(
                        HomeReportItemModel(
                            resourcesProvider.getString(R.string.totalSale),
                            it.invoiceCount,
                            it.invoiceAmount
                        )
                    )
                    items.add(
                        HomeReportItemModel(
                            resourcesProvider.getString(R.string.pluralReturn),
                            it.returnInvoiceCount,
                            it.returnInvoiceAmount
                        )
                    )
                    items.add(
                        HomeReportItemModel(
                            resourcesProvider.getString(R.string.pluralOrder),
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
                    items.add(
                        HomeReportItemModel(
                            resourcesProvider.getString(R.string.wareHouseStocks),
                            it.wareHouseStocks,
                            it.wareHouseStocksAmount
                        )
                    )
                    homeReportLiveData.postValue(items)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestFirstPageReport

}