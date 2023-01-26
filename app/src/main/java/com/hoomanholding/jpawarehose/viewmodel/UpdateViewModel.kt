package com.hoomanholding.jpawarehose.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.repository.*
import com.hoomanholding.jpawarehose.utility.SingleLiveEvent
import com.hoomanholding.jpawarehose.utility.hilt.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/26/2023
 */

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val brandRepository: BrandRepository,
    private val resourcesProvider: ResourcesProvider,
    private val productRepository: ProductRepository,
    private val supplierRepository: SupplierRepository,
    private val locationsRepository: LocationsRepository
) : JpaViewModel() {

    val successLiveData = SingleLiveEvent<String>()
    val brandLiveData = MutableLiveData(false)
    val productLiveData = MutableLiveData(false)
    val supplierLiveData = MutableLiveData(false)
    val locationLiveData = MutableLiveData(false)

    //---------------------------------------------------------------------------------------------- requestGetData
    fun requestGetData() {
        job?.cancel()
        job = CoroutineScope(IO).launch {
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
            val response = brandRepository.requestGetBrands()
            if (response?.isSuccessful == true) {
                val body = response.body()
                body?.let {
                    if (it.hasError)
                        setMessage(it.message)
                    else {
                        it.data?.let { brands ->
                            brandRepository.insertBrands(brands)
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
    //---------------------------------------------------------------------------------------------- getBrands


    //---------------------------------------------------------------------------------------------- getProducts
    private fun getProducts() : Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(100)
            productRepository.deleteAllProduct()
            delay(500)
            val response = productRepository.requestGetProducts()
            if (response?.isSuccessful == true) {
                val body = response.body()
                body?.let {
                    if (it.hasError)
                        setMessage(it.message)
                    else {
                        it.data?.let { products ->
                            productRepository.insertProducts(products)
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
    //---------------------------------------------------------------------------------------------- getProducts


    //---------------------------------------------------------------------------------------------- getLocations
    private fun getLocations() : Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(100)
            locationsRepository.deleteAllLocations()
            delay(500)
            val response = locationsRepository.requestGetLocations()
            if (response?.isSuccessful == true) {
                val body = response.body()
                body?.let {
                    if (it.hasError)
                        setMessage(it.message)
                    else {
                        it.data?.let { locations ->
                            locationsRepository.insertLocations(locations)
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
    //---------------------------------------------------------------------------------------------- getLocations



    //---------------------------------------------------------------------------------------------- getSuppliers
    private fun getSuppliers() : Job {
        return CoroutineScope(IO + exceptionHandler()).launch {
            delay(100)
            supplierRepository.deleteAllSuppliers()
            delay(500)
            val response = supplierRepository.requestGetSuppliers()
            if (response?.isSuccessful == true) {
                val body = response.body()
                body?.let {
                    if (it.hasError)
                        setMessage(it.message)
                    else {
                        it.data?.let { suppliers ->
                            supplierRepository.insertSuppliers(suppliers)
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
    //---------------------------------------------------------------------------------------------- getSuppliers


    //---------------------------------------------------------------------------------------------- getAllData
    private fun getAllData(): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            successLiveData.value = resourcesProvider.getString(R.string.updateIsSuccess)
        }
    }
    //---------------------------------------------------------------------------------------------- getAllData


}