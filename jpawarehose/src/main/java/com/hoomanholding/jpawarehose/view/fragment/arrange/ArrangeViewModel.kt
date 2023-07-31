package com.hoomanholding.jpawarehose.view.fragment.arrange

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.repository.receipt.ReceiptRepository
import com.hoomanholding.jpawarehose.view.adapter.holder.ReceiptProductHolder
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.applibrary.model.data.database.entity.LocationAmountEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptDetailEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptEntity
import com.hoomanholding.applibrary.model.data.database.join.ReceiptWithProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

@HiltViewModel
class ArrangeViewModel @Inject constructor(
    private val receiptRepository: ReceiptRepository
) : JpaViewModel() {

    val receiptLiveData = MutableLiveData<List<ReceiptEntity>>()
    val receiptDetailLiveData = MutableLiveData<List<ReceiptWithProduct>>()
    val locationFindLiveData = MutableLiveData<Int>()
    val sendReceiptToServer = MutableLiveData<String>()


    //---------------------------------------------------------------------------------------------- getOldData
    fun getOldData() {
        viewModelScope.launch(IO + exceptionHandler()){
            val details = receiptRepository.getReceiptDetail()
            if (details.isNotEmpty()) {
                val receipt = receiptRepository.getReceipt(details[0].id)
                receiptLiveData.postValue(listOf(receipt))
                receiptDetailLiveData.postValue(receiptRepository.getReceiptDetailJoin())
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getOldData


    //---------------------------------------------------------------------------------------------- requestGetReceipts
    fun requestGetReceipts() {
        viewModelScope.launch(IO + exceptionHandler()){
            callApi(
                request = receiptRepository.requestGetReceipts(),
                onReceiveData = { setReceipts(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetReceipts


    //---------------------------------------------------------------------------------------------- setReceipts
    private fun setReceipts(items: List<ReceiptEntity>) {
        viewModelScope.launch(IO + exceptionHandler()){
            receiptRepository.insertReceipts(items)
            delay(500)
            receiptLiveData.postValue(items)
        }
    }
    //---------------------------------------------------------------------------------------------- setReceipts


    //---------------------------------------------------------------------------------------------- requestGerReceiptDetail
    fun requestGerReceiptDetail(index: Int) {
        val receiptId = receiptLiveData.value?.get(index)?.id ?: 0
        if (receiptId == 0L)
            return
        viewModelScope.launch(IO + exceptionHandler()){
            callApi(
                request = receiptRepository.requestGerReceiptDetail(receiptId),
                onReceiveData = { setDetailReceipt(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestGerReceiptDetail


    //---------------------------------------------------------------------------------------------- setDetailReceipt
    private fun setDetailReceipt(items: List<ReceiptDetailEntity>){
        viewModelScope.launch(IO + exceptionHandler()){
            receiptRepository.insertReceiptDetail(items)
            delay(500)
            receiptDetailLiveData.postValue(receiptRepository.getReceiptDetailJoin())
        }
    }
    //---------------------------------------------------------------------------------------------- setDetailReceipt


    //---------------------------------------------------------------------------------------------- findLocation
    fun findLocation(id: Long) {
        viewModelScope.launch(IO + exceptionHandler()){
            var foundId: Long? = null
            val listProduct = receiptDetailLiveData.value
            listProduct?.let {
                for (i in it.indices) {
                    val listLocation = it[i].location
                    for (j in listLocation.indices) {
                        val location = listLocation[j]
                        if (location.locationEntity.locationId == id) {
                            ReceiptProductHolder.productPosition = i
                            ReceiptProductHolder.locationPosition = j
                            foundId = i.toLong()
                            locationFindLiveData.postValue(i)
                            cancel()
                            break
                        }
                    }
                }
                if (foundId == null)
                    setMessage(resourcesProvider.getString(R.string.qrCodeNotFoundInReceipt))
            }
        }
    }
    //---------------------------------------------------------------------------------------------- findLocation


    //---------------------------------------------------------------------------------------------- replaceOnLocation
    fun replaceOnLocation(amount: Int) {
        viewModelScope.launch(IO + exceptionHandler()){
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
                        receiptRepository.getSumAmount(receipt.productsEntity.id)
                    if (sum + amount > receipt.receiptDetailEntity.tedad) {
                        setMessage(resourcesProvider.getString(R.string.amountIsOverLoad))
                        return@launch
                    }
                }

                receiptRepository.insertLocationAmount(locationAmount)
            }
            ReceiptProductHolder.locationPosition = -1
            ReceiptProductHolder.productPosition = -1
            receiptDetailLiveData.postValue(receiptRepository.getReceiptDetailJoin())
        }
    }
    //---------------------------------------------------------------------------------------------- replaceOnLocation


    //---------------------------------------------------------------------------------------------- productCompletingOnReceipt
    fun productCompletingOnReceipt() {
        viewModelScope.launch(IO + exceptionHandler()){
            val listProduct = receiptDetailLiveData.value
            listProduct?.let { list ->
                for (product in list) {
                    val count = product.receiptDetailEntity.tedad
                    val productName = product.productsEntity.nameKala
                    val productId = product.productsEntity.id
                    val sum = receiptRepository.getSumAmount(productId)
                    if (count.toLong() != sum) {
                        setMessage(
                            resourcesProvider.getString(
                                R.string.ErrorOnCompletingOnReceipt,
                                productName
                            )
                        )
                        cancel()
                        break
                    }
                }
                callApi(
                    request = receiptRepository.requestConfirmReceipt(list[0].receiptDetailEntity.id),
                    onReceiveData = {
                        if (it)
                            sendReceiptToServer.
                            postValue(resourcesProvider.getString(R.string.saveReceiptSuccess))
                        else
                            receiptRepository.deleteAllReceiptDetailAndAmount()
                    }
                )
            }
        }
    }
    //---------------------------------------------------------------------------------------------- productCompletingOnReceipt


}