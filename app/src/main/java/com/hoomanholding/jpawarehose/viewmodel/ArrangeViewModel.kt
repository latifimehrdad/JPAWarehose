package com.hoomanholding.jpawarehose.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptDetailEntity
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptEntity
import com.hoomanholding.jpawarehose.model.database.join.ReceiptWithProduct
import com.hoomanholding.jpawarehose.model.repository.ReceiptRepository
import com.hoomanholding.jpawarehose.utility.hilt.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArrangeViewModel @Inject constructor(
    private val receiptRepository: ReceiptRepository,
    private val resourcesProvider: ResourcesProvider
) : JpaViewModel(){

    val receiptLiveData = MutableLiveData<List<ReceiptEntity>>()
    val receiptDetailLiveData = MutableLiveData<List<ReceiptWithProduct>>()


    //---------------------------------------------------------------------------------------------- getOldData
    fun getOldData() {
        CoroutineScope(IO + exceptionHandler()).launch {
            val details = receiptRepository.getReceiptDetail()
            if (details.isNotEmpty()) {
                val receipt = receiptRepository.getReceipt(details[0].id)
                withContext(Main) {
                    receiptDetailLiveData.value = receiptRepository.getReceiptDetailJoin()
                    receiptLiveData.value = listOf(receipt)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getOldData


    //---------------------------------------------------------------------------------------------- requestGetReceipts
    fun requestGetReceipts() {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val response = receiptRepository.requestGetReceipts()
            if (response?.isSuccessful == true) {
                val body = response.body()
                body?.let {
                    if (it.hasError)
                        setMessage(it.message)
                    else {
                        it.data?.let { receipt ->
                            receiptRepository.insertReceipts(receipt)
                            delay(500)
                            withContext(Main) {
                                receiptLiveData.value = receipt
                            }
                        } ?: run {
                            setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                        }
                    }
                } ?: run {
                    setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                }
            } else
                setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetReceipts


    //---------------------------------------------------------------------------------------------- requestGerReceiptDetail
    fun requestGerReceiptDetail(index : Int) {
        val id = receiptLiveData.value?.get(index)?.id ?: 0
        if (id == 0L)
            return
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val response = receiptRepository.requestGerReceiptDetail(id)
            if (response?.isSuccessful == true) {
                val body = response.body()
                body?.let {
                    if (it.hasError)
                        setMessage(it.message)
                    else {
                        it.data?.let { detail ->
                            receiptRepository.insertReceiptDetail(detail)
                            delay(500)
                            withContext(Main) {
                                receiptDetailLiveData.value =
                                    receiptRepository.getReceiptDetailJoin()
                            }
                        } ?: run {
                            setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                        }
                    }
                } ?: run {
                    setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                }
            } else
                setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestGerReceiptDetail

}