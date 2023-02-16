package com.hoomanholding.jpawarehose.view.fragment.savereceipt

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.data.request.AddWarehouseReceipt
import com.hoomanholding.jpawarehose.model.data.request.WarehouseReceiptItem
import com.hoomanholding.jpawarehose.model.data.enum.EnumSearchName
import com.hoomanholding.jpawarehose.model.data.enum.EnumSearchOrderType
import com.hoomanholding.jpawarehose.model.repository.*
import com.hoomanholding.jpawarehose.di.ResourcesProvider
import com.hoomanholding.jpawarehose.JpaViewModel
import com.hoomanholding.jpawarehose.model.data.database.entity.SaveReceiptAmountEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.SaveReceiptEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.model.data.database.join.ProductAmountModel
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import com.zar.core.tools.extensions.toSolarDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/26/2023
 */

@HiltViewModel
class SaveReceiptViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository,
    private val saveReceiptAmountRepository: SaveReceiptAmountRepository,
    private val saveReceiptRepository: SaveReceiptRepository,
    private val resourcesProvider: ResourcesProvider
) : JpaViewModel() {

    val supplierLiveData = MutableLiveData<List<SupplierEntity>>()
    val productLiveData = MutableLiveData<List<ProductAmountModel>>()
    var adapterNotifyChangeLiveData = MutableLiveData<Int>()
    val dateLiveData = MutableLiveData<String>()
    val receiptNumberLiveData = MutableLiveData<String?>()
    val sendReceiptToServer = MutableLiveData<String>()
    val searchProductLiveData = MutableLiveData<String>()
    val orderChangeLiveData = MutableLiveData(EnumSearchName.nameKala.name)
    var orderName = EnumSearchName.nameKala
    var orderType = EnumSearchOrderType.DESC
    private var supplierSelected: SupplierEntity? = null
    private var ignoreBrandId: Long? = null



    //---------------------------------------------------------------------------------------------- changeOrderType
    fun changeOrderType() {
        orderType = when(orderType) {
            EnumSearchOrderType.DESC -> EnumSearchOrderType.ASC
            EnumSearchOrderType.ASC -> EnumSearchOrderType.DESC
        }
        orderChangeLiveData.value = orderType.name
        searchProduct()
    }
    //---------------------------------------------------------------------------------------------- changeOrderType



    //---------------------------------------------------------------------------------------------- changeOrderType
    fun changeOrderName() {
        orderName = when(orderName) {
            EnumSearchName.codeKala -> EnumSearchName.nameKala
            EnumSearchName.nameKala -> EnumSearchName.codeKala
        }
        orderChangeLiveData.value = orderName.name
        searchProduct()
    }
    //---------------------------------------------------------------------------------------------- changeOrderType



    //---------------------------------------------------------------------------------------------- getSuppliers
    fun getSuppliers() {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            delay(300)
            val receipt = saveReceiptRepository.getSaveReceipt()
            val suppliers = receipt?.let {
                withContext(Main) {
                    dateLiveData.value = it.saveReceiptEntity.date
                    receiptNumberLiveData.value =
                        if (it.saveReceiptEntity.number != null)
                            it.saveReceiptEntity.number.toString() else ""
                }
                listOf(it.supplierEntity)
            } ?: run {
                withContext(Main) {
                    receiptNumberLiveData.value = null
                    dateLiveData.value = LocalDateTime.now().toSolarDate()?.getSolarDate()
                }
                supplierRepository.getSupplierFromDB()
            }
            withContext(Main) { supplierLiveData.value = suppliers }
        }
    }
    //---------------------------------------------------------------------------------------------- getSuppliers


    //---------------------------------------------------------------------------------------------- selectSupplier
    fun selectSupplier(index: Int) {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            supplierLiveData.value?.let {
                if (it.size > 1) saveReceiptAmountRepository.deleteAll()
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
        job = CoroutineScope(IO + exceptionHandler()).launch {
            if (ignoreBrandId == null)
                return@launch
            val search = searchProductLiveData.value
            val temp = if (search?.isEmpty() == true) {
                saveReceiptAmountRepository.search(
                    ignoreBrandId!!,
                    orderName.name,
                    orderType.name
                )
            } else {
                val words = search?.split(" ")
                saveReceiptAmountRepository.search(
                    ignoreBrandId!!,
                    orderName.name,
                    orderType.name,
                    words
                )
            }
            val products = temp.map { item ->
                ProductAmountModel(
                    item.productsEntity, item.saveReceiptAmountEntity, item.brandEntity
                )
            }
            Log.e("meri", "${products.size}")
            withContext(Main) { productLiveData.value = products }
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
            saveReceiptAmountRepository.insertSaveReceiptAmount(saveAmount)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.value = position
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
            saveReceiptAmountRepository.insertSaveReceiptAmount(saveAmount)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.value = position
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
                saveReceiptAmountRepository.insertSaveReceiptAmount(entity)
                insertNewSaveReceipt()
                adapterNotifyChangeLiveData.value = position
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
                saveReceiptAmountRepository.insertSaveReceiptAmount(entity)
                insertNewSaveReceipt()
                adapterNotifyChangeLiveData.value = position
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
            saveReceiptAmountRepository.insertSaveReceiptAmount(saveAmount)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.value = position
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
            saveReceiptAmountRepository.insertSaveReceiptAmount(saveAmount)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.value = position
        }
    }
    //---------------------------------------------------------------------------------------------- setPacket


    //---------------------------------------------------------------------------------------------- insertNewSaveReceipt
    private fun insertNewSaveReceipt() {
        if (saveReceiptRepository.getCount() > 0)
            return
        val date = LocalDateTime.now().toSolarDate()?.getSolarDate() ?: return
        val number =
            if (receiptNumberLiveData.value?.isDigitsOnly() == true) receiptNumberLiveData.value?.toLong()
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


    //---------------------------------------------------------------------------------------------- sendReceipt
    fun sendReceipt(description: String) {
        CoroutineScope(IO + exceptionHandler()).launch {
            val amounts = saveReceiptAmountRepository.getReceiptAmountWithProduct()
            if (amounts.isEmpty())
                setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
            else {
                val receipt = saveReceiptRepository.getSaveReceipt()
                receipt?.let {
                    if (receipt.saveReceiptEntity.number == null)
                        setMessage(resourcesProvider.getString(R.string.receiptNumberIsEmpty))
                    else {
                        val items = amounts.map {
                            val count = it.saveReceiptAmountEntity.cartonCount *
                                    it.productsEntity.tedadDarKarton +
                                    it.saveReceiptAmountEntity.packetCount
                            WarehouseReceiptItem(
                                it.productsEntity.id, count.toLong()
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
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val response = saveReceiptRepository.requestAddWarehouseReceipt(request)
            if (response?.isSuccessful == true) {
                val send = response.body()
                send?.let {
                    if (!it.hasError) {
                        saveReceiptAmountRepository.deleteAll()
                        saveReceiptRepository.deleteAllRecord()
                    }
                    withContext(Main) {
                        sendReceiptToServer.value = it.message
                    }
                } ?: run {
                    setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                }
            } else
                setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestAddWarehouseReceipt


    //---------------------------------------------------------------------------------------------- createNewReceipt
    fun createNewReceipt() {
        CoroutineScope(IO + exceptionHandler()).launch {
            saveReceiptAmountRepository.deleteAll()
            saveReceiptRepository.deleteAllRecord()
            supplierSelected = null
            delay(300)
            getSuppliers()

        }
    }
    //---------------------------------------------------------------------------------------------- createNewReceipt

}