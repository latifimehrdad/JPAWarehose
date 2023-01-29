package com.hoomanholding.jpawarehose.viewmodel

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.data.request.AddWarehouseReceipt
import com.hoomanholding.jpawarehose.model.data.request.WarehouseReceiptItem
import com.hoomanholding.jpawarehose.model.database.entity.ProductSaveReceiptEntity
import com.hoomanholding.jpawarehose.model.database.entity.SaveReceiptEntity
import com.hoomanholding.jpawarehose.model.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.model.repository.*
import com.hoomanholding.jpawarehose.utility.hilt.ResourcesProvider
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
    private val productSaveReceiptRepository: ProductSaveReceiptRepository,
    private val saveReceiptRepository: SaveReceiptRepository,
    private val resourcesProvider: ResourcesProvider
) : JpaViewModel() {

    val supplierLiveData = MutableLiveData<List<SupplierEntity>>()
    val productLiveData = MutableLiveData<List<ProductSaveReceiptEntity>>()
    var adapterNotifyChangeLiveData = MutableLiveData<Int>()
    val dateLiveData = MutableLiveData<String>()
    val receiptNumberLiveData = MutableLiveData<String?>()
    val sendReceiptToServer = MutableLiveData<String>()
    private var supplierSelected: SupplierEntity? = null


    //---------------------------------------------------------------------------------------------- getSuppliers
    fun getSuppliers() {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            delay(300)
            val receipt = saveReceiptRepository.getSaveReceipt()
            val suppliers = receipt?.let {
                withContext(Main) {
                    dateLiveData.value = it.date
                    receiptNumberLiveData.value =
                        if (it.number != null) it.number.toString() else ""
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
                if (it.size > 1) productSaveReceiptRepository.deleteAll()
            }
            var products = getProductFromDB()
            if (products.isEmpty()) {
                supplierSelected = supplierLiveData.value?.get(index)
                supplierSelected?.let {
                    val ignoreBrandId = if (it.id == 3180L) 2481L
                    else 2480L
                    products = productSaveReceiptRepository.getProductByIgnoreBrandId(ignoreBrandId)
                }
            }
            withContext(Main) { productLiveData.value = products }
        }
    }
    //---------------------------------------------------------------------------------------------- selectSupplier


    //---------------------------------------------------------------------------------------------- searchProduct
    fun searchProduct(search: String) {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val products = if (search.isEmpty())
                getProductFromDB()
            else {
                val words = search.split(" ")
                productSaveReceiptRepository.search(words)
            }
            withContext(Main) { productLiveData.value = products }
        }
    }
    //---------------------------------------------------------------------------------------------- searchProduct


    //---------------------------------------------------------------------------------------------- getProductFromDB
    private fun getProductFromDB() = productSaveReceiptRepository.getProductFromDB()
    //---------------------------------------------------------------------------------------------- getProductFromDB


    //---------------------------------------------------------------------------------------------- addCarton
    fun addCarton(position: Int) {
        productLiveData.value?.get(position)?.let {
            val count = it.cartonCount + 1
            it.cartonCount = count
            productSaveReceiptRepository.updateProduct(it)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.value = position
        }
    }
    //---------------------------------------------------------------------------------------------- addCarton


    //---------------------------------------------------------------------------------------------- addPacket
    fun addPacket(position: Int) {
        productLiveData.value?.get(position)?.let {
            val count = it.packetCount + 1
            it.packetCount = count
            productSaveReceiptRepository.updateProduct(it)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.value = position
        }
    }
    //---------------------------------------------------------------------------------------------- addPacket


    //---------------------------------------------------------------------------------------------- minusCarton
    fun minusCarton(position: Int) {
        productLiveData.value?.get(position)?.let {
            if (it.cartonCount == 0) return
            val count = it.cartonCount - 1
            it.cartonCount = count
            productSaveReceiptRepository.updateProduct(it)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.value = position
        }
    }
    //---------------------------------------------------------------------------------------------- minusCarton


    //---------------------------------------------------------------------------------------------- minusPacket
    fun minusPacket(position: Int) {
        productLiveData.value?.get(position)?.let {
            if (it.packetCount == 0) return
            val count = it.packetCount - 1
            it.packetCount = count
            productSaveReceiptRepository.updateProduct(it)
            insertNewSaveReceipt()
            adapterNotifyChangeLiveData.value = position
        }
    }
    //---------------------------------------------------------------------------------------------- minusPacket


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
                it, date, number
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
            if (productSaveReceiptRepository.getProductAmount() == 0)
                setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
            else {
                val receipt = saveReceiptRepository.getSaveReceipt()
                receipt?.let {
                    if (receipt.number == null)
                        setMessage(resourcesProvider.getString(R.string.receiptNumberIsEmpty))
                    else {
                        val products = productSaveReceiptRepository.getProductToSend()
                        val items = products.map {
                            val count = it.cartonCount *
                                    it.productWithBrandModel.productsEntity.tedadDarKarton +
                                    it.packetCount
                            WarehouseReceiptItem(
                                it.productWithBrandModel.productsEntity.id, count.toLong()
                            )
                        }
                        val request = AddWarehouseReceipt(
                            receipt.supplierEntity.id,
                            description,
                            receipt.number.toString(),
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
                        productSaveReceiptRepository.deleteAll()
                        saveReceiptRepository.deleteAllRecord()
                    }
                    withContext(Main){
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
            productSaveReceiptRepository.deleteAll()
            saveReceiptRepository.deleteAllRecord()
            supplierSelected = null
            delay(300)
            getSuppliers()

        }
    }
    //---------------------------------------------------------------------------------------------- createNewReceipt

}