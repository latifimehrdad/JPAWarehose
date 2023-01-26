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

}