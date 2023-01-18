package com.hoomanholding.jpawarehose.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.model.repository.SupplierRepository
import com.hoomanholding.jpawarehose.utility.hilt.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */

@HiltViewModel
class SupplierViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository,
    private val resourcesProvider: ResourcesProvider
) : JpaViewModel() {

    val suppliersLiveData = MutableLiveData<List<SupplierEntity>>()

    //---------------------------------------------------------------------------------------------- requestGetSuppliers
    fun requestGetSuppliers() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler()).launch {
            delay(500)
            val response = supplierRepository.requestGetSuppliers()
            if (response?.isSuccessful == true) {
                val userInfo = response.body()
                userInfo?.let {
                    if (it.hasError)
                        setMessage(it.message)
                    else {
                        it.data?.let { suppliers ->
                            supplierRepository.insertSuppliers(suppliers)
                            withContext(Main){suppliersLiveData.value = suppliers}
                            Log.e("meri", "requestGetSuppliers")
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
    //---------------------------------------------------------------------------------------------- requestGetSuppliers


}