package com.hoomanholding.jpamanager.view.fragment.report

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpamanager.JpaViewModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.model.data.other.CardBoardItemModel
import com.hoomanholding.jpawarehose.di.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * create by m-latifi on 3/6/2023
 */

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider
): JpaViewModel() {

    val itemLiveData: MutableLiveData<List<CardBoardItemModel>> by lazy {
        MutableLiveData<List<CardBoardItemModel>>()
    }

    //---------------------------------------------------------------------------------------------- setItems
    fun setItems() {
        val items: MutableList<CardBoardItemModel> = mutableListOf()
        items.add(
            CardBoardItemModel(
                R.drawable.ic_indebtedness,
                resourcesProvider.getString(R.string.indebtedness),
                0
            )
        )
        items.add(
            CardBoardItemModel(
                R.drawable.ic_sales_amount,
                resourcesProvider.getString(R.string.salesAmount),
                0
            )
        )
        items.add(
            CardBoardItemModel(
                R.drawable.ic_creditor,
                resourcesProvider.getString(R.string.creditor),
                0
            )
        )
        items.add(
            CardBoardItemModel(
                R.drawable.ic_indebtedness,
                resourcesProvider.getString(R.string.indebtedness),
                0
            )
        )
        items.add(
            CardBoardItemModel(
                R.drawable.ic_sales_amount,
                resourcesProvider.getString(R.string.salesAmount),
                0
            )
        )
        items.add(
            CardBoardItemModel(
                R.drawable.ic_creditor,
                resourcesProvider.getString(R.string.creditor),
                0
            )
        )


        itemLiveData.postValue(items)
    }
    //---------------------------------------------------------------------------------------------- setItems
}