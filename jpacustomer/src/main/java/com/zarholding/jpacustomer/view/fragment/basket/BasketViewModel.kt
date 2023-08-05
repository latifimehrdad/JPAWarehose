package com.zarholding.jpacustomer.view.fragment.basket

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.data.response.basket.DetailBasketModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 5/9/2023
 */

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val basketRepository: BasketRepository
) : JpaViewModel() {

    var productsList: List<DetailBasketModel>? = null
    var productSearch: String = ""
    val productLiveData: MutableLiveData<List<DetailBasketModel>> by lazy {
        MutableLiveData<List<DetailBasketModel>>()
    }
    val basketCountLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val addToBasketLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean> () }
    val submitBasketLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean> () }
    var productShowListType = false


    //---------------------------------------------------------------------------------------------- getProduct
    fun getProduct() {
        viewModelScope.launch(IO + exceptionHandler()) {
            productsList?.let {
                searchProduct(it)
            } ?: run {
                callApi(
                    request = basketRepository.requestGetDetailBasket(),
                    onReceiveData = {
                        it.items?.let {products ->
                            productsList = products
                            searchProduct(products)
                        }
                    }
                )
            }
            getBasketCount()
        }
    }
    //---------------------------------------------------------------------------------------------- getProduct



    //---------------------------------------------------------------------------------------------- setFilterByProductName
    fun setFilterByProductName(search: String) {
        productSearch = search
        getProduct()
    }
    //---------------------------------------------------------------------------------------------- setFilterByProductName



    //---------------------------------------------------------------------------------------------- searchProduct
    private fun searchProduct(items: List<DetailBasketModel>) {
        val words = productSearch.split(" ")
        val list = if (words.isEmpty())
            items
        else
            items.filter { product ->
                findWordInProductName(words, product)
            }
        productLiveData.postValue(list)
    }
    //---------------------------------------------------------------------------------------------- searchProduct


    //---------------------------------------------------------------------------------------------- findWordInProductName
    private fun findWordInProductName(words: List<String>, product: DetailBasketModel): Boolean {
        val text = "${product.productCode} ${product.productName}"
        for (word in words){
            if (!text.contains(word))
                return false
        }
        return true
    }
    //---------------------------------------------------------------------------------------------- findWordInProductName



    //---------------------------------------------------------------------------------------------- requestAddToBasket
    fun requestAddToBasket(request: AddToBasket) {
        viewModelScope.launch(IO + exceptionHandler()){
            callApi(
                request = basketRepository.requestAddToBasket(request),
                onReceiveData = {
                    addToBasketLiveData.postValue(it)
                    changeCount(request.ProductId, request.Count)
                }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestAddToBasket


    //---------------------------------------------------------------------------------------------- changeCount
    private fun changeCount(productId: Int, count: Int) {
        val item = productsList?.find {
            it.productId == productId
        }
        item?.count = count
    }
    //---------------------------------------------------------------------------------------------- changeCount



    //---------------------------------------------------------------------------------------------- requestDeleteBasket
    fun requestDeleteBasket(productId: Int) {
        viewModelScope.launch(IO + exceptionHandler()){
            callApi(
                request = basketRepository.requestDeleteBasket(productId),
                onReceiveData = {
                    productsList = null
                    getProduct()
                    getBasketCount()
                }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestDeleteBasket



    //---------------------------------------------------------------------------------------------- getBasketCount
    private fun getBasketCount() {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = basketRepository.requestGetBasketCount(),
                onReceiveData = { basketCountLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- getBasketCount


    //---------------------------------------------------------------------------------------------- requestSubmitBasket
    fun requestSubmitBasket(description: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = basketRepository.requestSubmitBasket(description),
                onReceiveData = { submitBasketLiveData.postValue(true) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestSubmitBasket

}