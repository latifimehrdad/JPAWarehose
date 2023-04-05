package com.hoomanholding.jpawarehose.view.fragment.savereceipt

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.applibrary.model.data.request.AddWarehouseReceipt
import com.hoomanholding.applibrary.model.data.request.WarehouseReceiptItem
import com.hoomanholding.applibrary.model.data.enums.EnumSearchName
import com.hoomanholding.applibrary.model.data.enums.EnumSearchOrderType
import com.hoomanholding.jpawarehose.model.repository.*
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.applibrary.model.data.database.entity.receipt.save.SaveReceiptAmountEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.save.SaveReceiptEntity
import com.hoomanholding.applibrary.model.data.database.entity.SupplierEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.history.HistorySaveReceiptAmountEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.history.HistorySaveReceiptEntity
import com.hoomanholding.applibrary.model.data.database.join.ProductAmountModel
import com.hoomanholding.jpawarehose.model.repository.receipt.SaveReceiptRepository
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import com.zar.core.tools.extensions.toSolarDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/26/2023
 */

@HiltViewModel
class SaveReceiptViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository,
    private val saveReceiptRepository: SaveReceiptRepository
) : JpaViewModel() {

    val supplierLiveData = MutableLiveData<List<SupplierEntity>>()
    val productLiveData = MutableLiveData<List<ProductAmountModel>>()
    var adapterNotifyChangeLiveData = MutableLiveData<Int>()
    val dateLiveData = MutableLiveData<String>()
    val receiptNumberLiveData = MutableLiveData<String?>()
    val sendReceiptToServer = MutableLiveData<String>()
    val searchProductLiveData = MutableLiveData<String>()
    val orderChangeLiveData = MutableLiveData(EnumSearchName.NameKala.name)
    var orderName = EnumSearchName.NameKala
    var orderType = EnumSearchOrderType.DESC
    private var supplierSelected: SupplierEntity? = null
    private var ignoreBrandId: Long? = null



    //---------------------------------------------------------------------------------------------- changeOrderType
    fun changeOrderType() {
        orderType = when(orderType) {
            EnumSearchOrderType.DESC -> EnumSearchOrderType.ASC
            EnumSearchOrderType.ASC -> EnumSearchOrderType.DESC
        }
        orderChangeLiveData.postValue(orderType.name)
        searchProduct()
    }
    //---------------------------------------------------------------------------------------------- changeOrderType



    //---------------------------------------------------------------------------------------------- changeOrderType
    fun changeOrderName() {
        orderName = when(orderName) {
            EnumSearchName.CodeKala -> EnumSearchName.NameKala
            EnumSearchName.NameKala -> EnumSearchName.CodeKala
        }
        orderChangeLiveData.postValue(orderName.name)
        searchProduct()
    }
    //---------------------------------------------------------------------------------------------- changeOrderType



    //---------------------------------------------------------------------------------------------- getSuppliers
    fun getSuppliers() {
        viewModelScope.launch(IO + exceptionHandler()){
            delay(300)
            val receipt = saveReceiptRepository.getSaveReceipt()
            val suppliers = receipt?.let {
                dateLiveData.postValue(it.saveReceiptEntity.date)
                val number = if (it.saveReceiptEntity.number != null)
                    it.saveReceiptEntity.number.toString() else ""
                receiptNumberLiveData.postValue(number)
                listOf(it.supplierEntity)
            } ?: run {
                receiptNumberLiveData.postValue(null)
                dateLiveData.postValue(LocalDateTime.now().toSolarDate()?.getSolarDate())
                supplierRepository.getSupplierFromDB()
            }
            supplierLiveData.postValue(suppliers)
        }
    }
    //---------------------------------------------------------------------------------------------- getSuppliers


    //---------------------------------------------------------------------------------------------- selectSupplier
    fun selectSupplier(index: Int) {
        viewModelScope.launch(IO + exceptionHandler()){
            supplierLiveData.value?.let {
                if (it.size > 1) saveReceiptRepository.deleteAllAmount()
            }
            supplierSelected = supplierLiveData.value?.get(index)
            supplierSelected?.let {
                ignoreBrandId = if (it.id == 3180L) 2481L
                else 2480L
                searchProduct()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- selectSupplier


    //---------------------------------------------------------------------------------------------- searchProduct
    fun searchProduct() {
        if (supplierSelected == null)
            return
        viewModelScope.launch(IO + exceptionHandler()){
            if (ignoreBrandId == null)
                return@launch
            val search = searchProductLiveData.value
            val temp = if (search?.isEmpty() == true) {
                saveReceiptRepository.search(
                    ignoreBrandId!!,
                    orderName.name,
                    orderType.name
                )
            } else {
                val words = search?.split(" ")
                saveReceiptRepository.search(
                    ignoreBrandId!!,
                    orderName.name,
                    orderType.name,
                    words
                )
            }
            val temp2 = temp.sortedBy { item ->
                item.saveReceiptAmountEntity?.let {
                    (it.cartonCount == 0 && it.packetCount == 0)
                } ?: run { true }
            }
            val products = temp2.map { item ->
                ProductAmountModel(
                    item.productsEntity, item.saveReceiptAmountEntity, item.brandEntity
                )
            }
            productLiveData.postValue(products)
        }
    }
    //---------------------------------------------------------------------------------------------- searchProduct


    //---------------------------------------------------------------------------------------------- addCarton
    fun addCarton(position: Int) {
        productLiveData.value?.get(position)?.let {
            val saveAmount = it.saveReceiptAmountEntity?.let { amount ->
                val count = amount.cartonCount + 1
                amount.cartonCount = count
                amount
            } ?: run {
                SaveReceiptAmountEntity(
                    it.productsEntity.id,
                    1, 0
                )
            }
            productLiveData.value?.get(position)?.saveReceiptAmountEntity = saveAmount
            saveReceiptRepository.insertSaveReceiptAmount(saveAmount)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.postValue(position)
        }
    }
    //---------------------------------------------------------------------------------------------- addCarton


    //---------------------------------------------------------------------------------------------- addPacket
    fun addPacket(position: Int) {
        productLiveData.value?.get(position)?.let {
            val saveAmount = it.saveReceiptAmountEntity?.let { amount ->
                val count = amount.packetCount + 1
                amount.packetCount = count
                amount
            } ?: run {
                SaveReceiptAmountEntity(
                    it.productsEntity.id,
                    0, 1
                )
            }
            productLiveData.value?.get(position)?.saveReceiptAmountEntity = saveAmount
            saveReceiptRepository.insertSaveReceiptAmount(saveAmount)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.postValue(position)
        }
    }
    //---------------------------------------------------------------------------------------------- addPacket


    //---------------------------------------------------------------------------------------------- minusCarton
    fun minusCarton(position: Int) {
        productLiveData.value?.get(position)?.let {
            it.saveReceiptAmountEntity?.let { entity ->
                val count = entity.cartonCount - 1
                if (count < 0)
                    return
                entity.cartonCount = count
                saveReceiptRepository.insertSaveReceiptAmount(entity)
                insertNewSaveReceipt()
                adapterNotifyChangeLiveData.postValue(position)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- minusCarton


    //---------------------------------------------------------------------------------------------- minusPacket
    fun minusPacket(position: Int) {
        productLiveData.value?.get(position)?.let {
            it.saveReceiptAmountEntity?.let { entity ->
                val count = entity.packetCount - 1
                if (count < 0)
                    return
                entity.packetCount = count
                saveReceiptRepository.insertSaveReceiptAmount(entity)
                insertNewSaveReceipt()
                adapterNotifyChangeLiveData.postValue(position)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- minusPacket


    //---------------------------------------------------------------------------------------------- setCarton
    fun setCarton(position: Int, amount: Int) {
        productLiveData.value?.get(position)?.let {
            val saveAmount = it.saveReceiptAmountEntity?.let { entity ->
                entity.cartonCount = amount
                entity
            } ?: run {
                SaveReceiptAmountEntity(
                    it.productsEntity.id,
                    amount, 0
                )
            }
            productLiveData.value?.get(position)?.saveReceiptAmountEntity = saveAmount
            saveReceiptRepository.insertSaveReceiptAmount(saveAmount)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.postValue(position)
        }
    }
    //---------------------------------------------------------------------------------------------- setCarton


    //---------------------------------------------------------------------------------------------- setPacket
    fun setPacket(position: Int, amount: Int) {
        productLiveData.value?.get(position)?.let {
            val saveAmount = it.saveReceiptAmountEntity?.let { entity ->
                entity.packetCount = amount
                entity
            } ?: run {
                SaveReceiptAmountEntity(
                    it.productsEntity.id,
                    0, amount
                )
            }
            productLiveData.value?.get(position)?.saveReceiptAmountEntity = saveAmount
            saveReceiptRepository.insertSaveReceiptAmount(saveAmount)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.postValue(position)
        }
    }
    //---------------------------------------------------------------------------------------------- setPacket


    //---------------------------------------------------------------------------------------------- insertNewSaveReceipt
    private fun insertNewSaveReceipt() {
        if (saveReceiptRepository.getCount() > 0)
            return
        val date = LocalDateTime.now().toSolarDate()?.getSolarDate() ?: return
        val number =
            if(receiptNumberLiveData.value?.isEmpty() == true)
                null
            else if (receiptNumberLiveData.value?.isDigitsOnly() == true)
                receiptNumberLiveData.value?.toLong()
            else null
        supplierSelected?.let {
            val saveReceiptEntity = SaveReceiptEntity(
                it.id, date, number
            )
            saveReceiptRepository.insertSaveReceipts(saveReceiptEntity)
        }
    }
    //---------------------------------------------------------------------------------------------- insertNewSaveReceipt


    //---------------------------------------------------------------------------------------------- updateLastSaveReceipt
    fun updateLastSaveReceipt(receiptNumber: String) {
        val number = receiptNumber.persianNumberToEnglishNumber()
        if (number.isDigitsOnly()) {
            saveReceiptRepository.updateReceiptNumber(number.toLong())
        }
    }
    //---------------------------------------------------------------------------------------------- updateLastSaveReceipt


    //---------------------------------------------------------------------------------------------- getReceiptAmountWithProduct
    fun getReceiptAmountWithProduct() = saveReceiptRepository.getReceiptAmountWithProduct()
    //---------------------------------------------------------------------------------------------- getReceiptAmountWithProduct


    //---------------------------------------------------------------------------------------------- sendReceipt
    fun sendReceipt(description: String) {
        CoroutineScope(IO + exceptionHandler()).launch {
            val amounts = getReceiptAmountWithProduct()
            if (amounts.isEmpty())
                setMessage(resourcesProvider.getString(
                    com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                ))
            else {
                val receipt = saveReceiptRepository.getSaveReceipt()
                receipt?.let {
                    if (receipt.saveReceiptEntity.number == null)
                        setMessage(resourcesProvider.getString(R.string.receiptNumberIsEmpty))
                    else {
                        val items = amounts.map {
                            val count = it.saveReceiptAmountEntity.cartonCount *
                                    it.products.productsEntity.tedadDarKarton +
                                    it.saveReceiptAmountEntity.packetCount
                            WarehouseReceiptItem(
                                it.products.productsEntity.id, count.toLong()
                            )
                        }
                        val request = AddWarehouseReceipt(
                            receipt.supplierEntity.id,
                            description,
                            receipt.saveReceiptEntity.number.toString(),
                            items
                        )
                        requestAddWarehouseReceipt(request)
                    }
                }

            }
        }
    }
    //---------------------------------------------------------------------------------------------- sendReceipt


    //---------------------------------------------------------------------------------------------- requestAddWarehouseReceipt
    private fun requestAddWarehouseReceipt(request: AddWarehouseReceipt) {
        viewModelScope.launch(IO + exceptionHandler()){
            val response = checkResponse(saveReceiptRepository.requestAddWarehouseReceipt(request))
            response?.let { addToHistoryOfSaveReceipt(request.description) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestAddWarehouseReceipt



    //---------------------------------------------------------------------------------------------- addToHistoryOfSaveReceipt
    private fun addToHistoryOfSaveReceipt(description: String?){
        val saveReceipt = saveReceiptRepository.getSaveReceipt()
        saveReceipt?.let {
            val saveReceiptAmount = getReceiptAmountWithProduct()
            val model = HistorySaveReceiptEntity(
                saveReceipt.saveReceiptEntity.date,
                saveReceipt.saveReceiptEntity.number,
                description,
                saveReceipt.supplierEntity.id
            )
            val receiptId = saveReceiptRepository.insertHistorySaveReceipt(model)
            val amounts = saveReceiptAmount.map { item ->
                HistorySaveReceiptAmountEntity(
                    item.saveReceiptAmountEntity.cartonCount,
                    item.saveReceiptAmountEntity.packetCount,
                    item.products.productsEntity.id,
                    receiptId
                )
            }
            saveReceiptRepository.insertHistorySaveReceiptAmount(amounts)
        }
        saveReceiptRepository.deleteAllAmount()
        saveReceiptRepository.deleteAllRecord()
        sendReceiptToServer.postValue(resourcesProvider.getString(R.string.saveReceiptSuccess))
    }
    //---------------------------------------------------------------------------------------------- addToHistoryOfSaveReceipt


    //---------------------------------------------------------------------------------------------- createNewReceipt
    fun createNewReceipt() {
        CoroutineScope(IO + exceptionHandler()).launch {
            saveReceiptRepository.deleteAllAmount()
            saveReceiptRepository.deleteAllRecord()
            supplierSelected = null
            delay(300)
            getSuppliers()

        }
    }
    //---------------------------------------------------------------------------------------------- createNewReceipt

}