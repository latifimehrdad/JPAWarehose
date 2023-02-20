package com.hoomanholding.jpawarehose.view.fragment.savereceipt.history

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.JpaViewModel
import com.hoomanholding.jpawarehose.model.data.database.join.HistorySaveReceiptWithSupplier
import com.hoomanholding.jpawarehose.model.repository.receipt.SaveReceiptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by zar on 2/20/2023.
 */

@HiltViewModel
class HistorySaveReceiptViewModel @Inject constructor(
    private val saveReceiptRepository: SaveReceiptRepository
): JpaViewModel() {

    val saveReceiptsLiveData: MutableLiveData<List<HistorySaveReceiptWithSupplier>> by lazy {
        MutableLiveData()
    }

    //---------------------------------------------------------------------------------------------- getSaveReceipt
    fun getSaveReceipts() {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            saveReceiptsLiveData.postValue(saveReceiptRepository.getHistorySaveReceipts())
        }
    }
    //---------------------------------------------------------------------------------------------- getSaveReceipt



}