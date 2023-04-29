package com.hoomanholding.jpamanager.view.fragment.report

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.model.data.other.CardBoardItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel


/**
 * create by m-latifi on 3/6/2023
 */

@HiltViewModel
class ReportViewModel @Inject constructor(): JpaViewModel() {

    val itemLiveData: MutableLiveData<List<CardBoardItemModel>> by lazy {
        MutableLiveData<List<CardBoardItemModel>>()
    }

    //---------------------------------------------------------------------------------------------- setItems
    fun setItems(tabPosition: Int) {
        when(tabPosition){
            0 -> setHrReportItem()
            1 -> setSaleReportItem()
            2 -> setFinancialReportItem()
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
        itemLiveData.postValue(items)
    }
    //---------------------------------------------------------------------------------------------- setFinancialReportItem

}