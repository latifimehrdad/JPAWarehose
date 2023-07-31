package com.zarholding.jpacustomer.view.fragment.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import com.zarholding.jpacustomer.model.repository.BasketRepository
import com.zarholding.jpacustomer.model.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 5/9/2023
 */

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val basketRepository: BasketRepository
) : JpaViewModel() {

    private var productsList: List<ProductModel>? = null
    var productSearch: String = ""
    val productNewLiveData = MutableLiveData(false)
    val productLiveData: MutableLiveData<List<ProductModel>> by lazy {
        MutableLiveData<List<ProductModel>>()
    }
    val basketCountLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    //---------------------------------------------------------------------------------------------- getProduct
    fun getProduct() {
        viewModelScope.launch(IO + exceptionHandler()) {
            productsList?.let {
                searchProduct(it)
            } ?: run {
                callApi(
                    request = productRepository.requestGetCustomerProducts(),
                    onReceiveData = {products ->
                        productsList = products
                        searchProduct(products)
                    }
                )
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getProduct


    //---------------------------------------------------------------------------------------------- setFilterByProductNew
    fun setFilterByProductNew(new: Boolean) {
        viewModelScope.launch(IO + exceptionHandler()) {
            productNewLiveData.postValue(new)
            delay(500)
            getProduct()
        }
    }
    //---------------------------------------------------------------------------------------------- setFilterByProductNew


    //---------------------------------------------------------------------------------------------- setFilterByProductName
    fun setFilterByProductName(search: String) {
        productSearch = search
        getProduct()
    }
    //---------------------------------------------------------------------------------------------- setFilterByProductName



    //---------------------------------------------------------------------------------------------- searchProduct
    private fun searchProduct(items: List<ProductModel>) {
        val new = productNewLiveData.value ?: false
        productSearch = productSearch.persianNumberToEnglishNumber()
        val words = productSearch.split(" ")
        val list = if (new)
            if (words.isEmpty())
                items.filter { product ->
                    product.isNew
                }
            else
                items.filter { product ->
                    product.isNew && findWordInProductName(words, product)
                }
        else
            if (words.isEmpty())
                items
            else
                items.filter { product ->
                    findWordInProductName(words, product)
                }
        productLiveData.postValue(list)
    }
    //---------------------------------------------------------------------------------------------- searchProduct


    //---------------------------------------------------------------------------------------------- findWordInProductName
    private fun findWordInProductName(words: List<String>, product: ProductModel): Boolean {
        val text = "${product.productCode} ${product.productName}"
        for (word in words){
            if (!text.contains(word))
                return false
        }
        return true
    }
    //---------------------------------------------------------------------------------------------- findWordInProductName


    //---------------------------------------------------------------------------------------------- getBasketCount
    fun getBasketCount() {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = basketRepository.requestGetBasketCount(),
                onReceiveData = { basketCountLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- getBasketCount
}