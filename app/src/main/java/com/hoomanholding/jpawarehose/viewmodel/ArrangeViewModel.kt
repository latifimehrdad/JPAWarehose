package com.hoomanholding.jpawarehose.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.database.entity.LocationAmountEntity
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptEntity
import com.hoomanholding.jpawarehose.model.database.join.ReceiptWithProduct
import com.hoomanholding.jpawarehose.model.repository.ReceiptRepository
import com.hoomanholding.jpawarehose.utility.hilt.ResourcesProvider
import com.hoomanholding.jpawarehose.view.adapter.holder.ReceiptProductHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

@HiltViewModel
class ArrangeViewModel @Inject constructor(
    private val receiptRepository: ReceiptRepository,
    private val resourcesProvider: ResourcesProvider
) : JpaViewModel() {

    val receiptLiveData = MutableLiveData<List<ReceiptEntity>>()
    val receiptDetailLiveData = MutableLiveData<List<ReceiptWithProduct>>()
    val locationFindLiveData = MutableLiveData<Int>()


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
    fun requestGerReceiptDetail(index: Int) {
        val receiptId = receiptLiveData.value?.get(index)?.id ?: 0
        if (receiptId == 0L)
            return
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val response = receiptRepository.requestGerReceiptDetail(receiptId)
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


    //---------------------------------------------------------------------------------------------- findLocation
    fun findLocation(id: Long) {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val listProduct = receiptDetailLiveData.value
            listProduct?.let {
                for (i in it.indices) {
                    val listLocation = it[i].location
                    for (j in listLocation.indices) {
                        val location = listLocation[j]
                        if (location.locationEntity.locationId == id) {
                            ReceiptProductHolder.productPosition = i
                            ReceiptProductHolder.locationPosition = j
                            withContext(Main) {
                                locationFindLiveData.value = i

                            }
                            cancel()
                            break
                        }
                    }
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- findLocation


    //---------------------------------------------------------------------------------------------- replaceOnLocation
    fun replaceOnLocation(amount: Int) {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val listProduct = receiptDetailLiveData.value
            listProduct?.let {
                val receipt = it[ReceiptProductHolder.productPosition]
                val listLocation = receipt.location
                var locationId: Long? = null
                val location = listLocation[ReceiptProductHolder.locationPosition]
                val locationAmount = location.locationAmount?.let { item ->
                    locationId = item.locationId
                    item.amount = amount
                    item
                } ?: run {
                    LocationAmountEntity(
                        location.locationEntity.locationId,
                        it[ReceiptProductHolder.productPosition].productsEntity.id,
                        amount
                    )
                }
                if (receipt.location.size == 1) {
                    if (receipt.receiptDetailEntity.tedad > amount) {
                        setMessage(resourcesProvider.getString(R.string.amountIsLessThenAmount))
                        return@launch
                    } else if (receipt.receiptDetailEntity.tedad < amount) {
                        setMessage(resourcesProvider.getString(R.string.amountIsOverLoad))
                        return@launch
                    }
                } else {
                    val sum = if (locationId != null)
                        receiptRepository.getSumAmount(locationId!!, receipt.productsEntity.id)
                    else
                        receiptRepository.getSumAmount()
                    if (sum + amount > receipt.receiptDetailEntity.tedad) {
                        setMessage(resourcesProvider.getString(R.string.amountIsOverLoad))
                        return@launch
                    }
                }

                receiptRepository.insertLocationAmount(locationAmount)
            }
            ReceiptProductHolder.locationPosition = -1
            ReceiptProductHolder.productPosition = -1
            withContext(Main) {
                receiptDetailLiveData.value =
                    receiptRepository.getReceiptDetailJoin()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- replaceOnLocation

}