package com.hoomanholding.jpawarehose.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.repository.ProductRepository
import com.hoomanholding.jpawarehose.utility.hilt.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val resourcesProvider: ResourcesProvider
) : JpaViewModel() {

    val productsLiveData = MutableLiveData<List<ProductsEntity>>()

    //---------------------------------------------------------------------------------------------- requestGetProducts
    fun requestGetProducts() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler()).launch {
            delay(500)
            val response = productRepository.requestGetProducts()
            if (response?.isSuccessful == true) {
                val userInfo = response.body()
                userInfo?.let {
                    if (it.hasError)
                        setMessage(it.message)
                    else {
                        it.data?.let { products ->
                            productRepository.insertProducts(products)
                            withContext(Dispatchers.Main){productsLiveData.value = products}
                            Log.e("meri", "requestGetProducts")
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
    //---------------------------------------------------------------------------------------------- requestGetProducts


}