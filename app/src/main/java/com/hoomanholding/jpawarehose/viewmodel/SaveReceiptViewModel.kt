package com.hoomanholding.jpawarehose.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.database.join.ProductWithBrandModel
import com.hoomanholding.jpawarehose.model.repository.BrandRepository
import com.hoomanholding.jpawarehose.model.repository.ProductRepository
import com.hoomanholding.jpawarehose.model.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/26/2023
 */

@HiltViewModel
class SaveReceiptViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository
) : JpaViewModel(){

    val productLiveData = MutableLiveData<List<ProductWithBrandModel>>()
    var adapterNotifyChangeLiveData = MutableLiveData<Int>()

    //---------------------------------------------------------------------------------------------- getSuppliers
    fun getSuppliers() = supplierRepository.getSupplierFromDB()
    //---------------------------------------------------------------------------------------------- getSuppliers


    //---------------------------------------------------------------------------------------------- getBrands
    fun getBrands() = brandRepository.getBrands()
    //---------------------------------------------------------------------------------------------- getBrands


    //---------------------------------------------------------------------------------------------- getProductByIgnoreBrandId
    fun getProductByIgnoreBrandId(ignoreBrandId : Long) {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val product = productRepository.getProductByIgnoreBrandId(ignoreBrandId)
            withContext(Main){ productLiveData.value = product }
        }
    }
    //---------------------------------------------------------------------------------------------- getProductByIgnoreBrandId


    //---------------------------------------------------------------------------------------------- addCarton
    fun addCarton(position: Int) {
        productLiveData.value?.get(position)?.productsEntity?.let {
            val count = it.cartonCount + 1
            it.cartonCount = count
            productRepository.updateProduct(it)
            adapterNotifyChangeLiveData.value = position
        }
    }
    //---------------------------------------------------------------------------------------------- addCarton


    //---------------------------------------------------------------------------------------------- addPacket
    fun addPacket(position: Int) {
        productLiveData.value?.get(position)?.productsEntity?.let {
            val count = it.packetCount + 1
            it.packetCount = count
            productRepository.updateProduct(it)
            adapterNotifyChangeLiveData.value = position
        }
    }
    //---------------------------------------------------------------------------------------------- addPacket


    //---------------------------------------------------------------------------------------------- minusCarton
    fun minusCarton(position: Int) {
        productLiveData.value?.get(position)?.productsEntity?.let {
            if (it.cartonCount == 0)
                return
            val count = it.cartonCount - 1
            it.cartonCount = count
            productRepository.updateProduct(it)
            adapterNotifyChangeLiveData.value = position
        }
    }
    //---------------------------------------------------------------------------------------------- minusCarton


    //---------------------------------------------------------------------------------------------- minusPacket
    fun minusPacket(position: Int) {
        productLiveData.value?.get(position)?.productsEntity?.let {
            if (it.packetCount == 0)
                return
            val count = it.packetCount - 1
            it.packetCount = count
            productRepository.updateProduct(it)
            adapterNotifyChangeLiveData.value = position
        }
    }
    //---------------------------------------------------------------------------------------------- minusPacket

}