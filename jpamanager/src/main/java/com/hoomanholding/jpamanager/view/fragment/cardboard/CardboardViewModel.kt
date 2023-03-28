package com.hoomanholding.jpamanager.view.fragment.cardboard

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
class CardboardViewModel @Inject constructor(
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
                R.drawable.ic_credit_card,
                resourcesProvider.getString(R.string.pinCard),
                0
            )
        )
        items.add(
            CardBoardItemModel(
                R.drawable.ic_invoice,
                resourcesProvider.getString(R.string.invoice),
                R.id.action_splashFragment_to_InvoiceFragment
            )
        )
        items.add(
            CardBoardItemModel(
                R.drawable.ic_ckeck,
                resourcesProvider.getString(R.string.check),
                0
            )
        )
        itemLiveData.postValue(items)
    }
    //---------------------------------------------------------------------------------------------- setItems
}