package com.hoomanholding.jpawarehose.view.fragment.update

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.repository.*
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/26/2023
 */

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
    private val supplierRepository: SupplierRepository,
    private val locationsRepository: LocationsRepository
) : JpaViewModel() {

    val successLiveData =
        SingleLiveEvent<String>()
    val brandLiveData = MutableLiveData(false)
    val productLiveData = MutableLiveData(false)
    val supplierLiveData = MutableLiveData(false)
    val locationLiveData = MutableLiveData(false)
    val percentUpdatingLiveData = MutableLiveData(0)
    private var countUpdate = 0
    private var percentUpdate = 0

    //---------------------------------------------------------------------------------------------- requestGetData
    fun requestGetData() {
        countUpdate = 0
        percentUpdate = 0
        if (brandLiveData.value == true)
            countUpdate++
        if (productLiveData.value == true)
            countUpdate++
        if (locationLiveData.value == true)
            countUpdate++
        if (supplierLiveData.value == true)
            countUpdate++
        job?.cancel()
        job = CoroutineScope(IO).launch {
            percentUpdatingLiveData.postValue(0)
            if (brandLiveData.value == true)
                getBrands().join()
            if (productLiveData.value == true)
                getProducts().join()
            if (locationLiveData.value == true)
                getLocations().join()
            if (supplierLiveData.value == true)
                getSuppliers().join()

            getAllData().join()
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetData


    //---------------------------------------------------------------------------------------------- getBrands
    private fun getBrands(): Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(100)
            brandRepository.deleteAllBrands()
            delay(500)
            val response = checkResponse(brandRepository.requestGetBrands())
            response?.let {
                brandRepository.insertBrands(it)
                serPercentOfUpdate()
            }
        }

    }
    //---------------------------------------------------------------------------------------------- getBrands


    //---------------------------------------------------------------------------------------------- getProducts
    private fun getProducts() : Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(100)
            productRepository.deleteAllProduct()
            delay(500)
            val response = checkResponse(productRepository.requestGetProducts())
            response?.let {
                productRepository.insertProducts(it)
                serPercentOfUpdate()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getProducts


    //---------------------------------------------------------------------------------------------- getLocations
    private fun getLocations() : Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(100)
            locationsRepository.deleteAllLocations()
            delay(500)
            val response = checkResponse(locationsRepository.requestGetLocations())
            response?.let {
                locationsRepository.insertLocations(it)
                serPercentOfUpdate() }
        }
    }
    //---------------------------------------------------------------------------------------------- getLocations



    //---------------------------------------------------------------------------------------------- getSuppliers
    private fun getSuppliers() : Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(100)
            supplierRepository.deleteAllSuppliers()
            delay(500)
            val response = checkResponse(supplierRepository.requestGetSuppliers())
            response?.let {
                supplierRepository.insertSuppliers(it)
                serPercentOfUpdate()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getSuppliers


    //---------------------------------------------------------------------------------------------- serPercentOfUpdate
    private fun serPercentOfUpdate() {
        percentUpdate++
        percentUpdatingLiveData.postValue((percentUpdate / countUpdate.toFloat() * 100).toInt())
    }
    //---------------------------------------------------------------------------------------------- serPercentOfUpdate


    //---------------------------------------------------------------------------------------------- getAllData
    private fun getAllData(): Job {
        return CoroutineScope(Main).launch {
            successLiveData.value = resourcesProvider.getString(R.string.updateIsSuccess)
        }
    }
    //---------------------------------------------------------------------------------------------- getAllData


}