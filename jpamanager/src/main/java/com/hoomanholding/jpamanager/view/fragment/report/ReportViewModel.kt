package com.hoomanholding.jpamanager.view.fragment.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.model.data.other.CardBoardItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * create by m-latifi on 3/6/2023
 */

@HiltViewModel
class ReportViewModel @Inject constructor(): JpaViewModel() {


    val itemPositionLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    val itemLiveData: MutableLiveData<List<CardBoardItemModel>?> by lazy {
        MutableLiveData<List<CardBoardItemModel>?>()
    }

    //---------------------------------------------------------------------------------------------- setItems
    fun setItems(tabPosition: Int) {
        viewModelScope.launch {
            itemPositionLiveData.postValue(tabPosition)
            delay(200)
            when(tabPosition){
                0 -> setHrReportItem()
                1 -> setSaleReportItem()
                2 -> setFinancialReportItem()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setItems


    //---------------------------------------------------------------------------------------------- setSaleReportItem
    private fun setSaleReportItem() {
        val items: MutableList<CardBoardItemModel> = mutableListOf()
        items.add(
            CardBoardItemModel(
                R.drawable.ic_sales_amount,
                resourcesProvider.getString(R.string.salesAmount),
                R.id.action_reportFragment_to_visitorSaleReportFragment
            )
        )
        itemLiveData.postValue(items)
    }
    //---------------------------------------------------------------------------------------------- setSaleReportItem



    //---------------------------------------------------------------------------------------------- setHrReportItem
    private fun setHrReportItem() {
        itemLiveData.postValue(null)
    }
    //---------------------------------------------------------------------------------------------- setHrReportItem



    //---------------------------------------------------------------------------------------------- setFinancialReportItem
    private fun setFinancialReportItem() {
        val items: MutableList<CardBoardItemModel> = mutableListOf()
        items.add(
            CardBoardItemModel(
                R.drawable.ic_creditor,
                resourcesProvider.getString(R.string.financialReport),
                R.id.action_reportFragment_to_customerBalanceFragment
            )
        )

        items.add(
            CardBoardItemModel(
                R.drawable.ic_ckeck,
                resourcesProvider.getString(R.string.bouncedCheck),
                R.id.action_reportFragment_to_customerBounceCheckFragment
            )
        )
        itemLiveData.postValue(items)
    }
    //---------------------------------------------------------------------------------------------- setFinancialReportItem

}