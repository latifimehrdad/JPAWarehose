package com.zarholding.jpacustomer.view.fragment.basket

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.data.response.basket.BasketModel
import com.hoomanholding.applibrary.model.data.response.basket.DetailBasketModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.EnumProductPageType
import com.zarholding.jpacustomer.model.repository.BasketRepository
import com.zarholding.jpacustomer.view.fragment.product.ProductViewModel
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

    private var productPageType: EnumProductPageType = EnumProductPageType.Product
    var productsList: List<DetailBasketModel>? = null
    var productSearch: String = ""
    val productLiveData: MutableLiveData<List<DetailBasketModel>> by lazy {
        MutableLiveData<List<DetailBasketModel>>()
    }
    val basketCountLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val addToBasketLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean> () }
    val submitBasketLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean> () }
    var productShowListType = false
    var isExhibitionActive: Boolean = false


    //---------------------------------------------------------------------------------------------- getBasket
    fun getBasket(type: EnumProductPageType) {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (type == productPageType && productsList != null)
                searchProduct(productsList!!)
            else {
                productPageType = type
                when (type) {
                    EnumProductPageType.Product -> callApi(
                        request = basketRepository.requestGetDetailBasket(),
                        onReceiveData = {
                            isExhibitionActive = it.isExhibitionActive
                            it.items?.let {products ->
                                productsList = products
                                searchProduct(products)
                            }
                        }
                    )

                    EnumProductPageType.Return -> callApi(
                        request = basketRepository.requestGetDetailReturnBasket(),
                        onReceiveData = {
                            isExhibitionActive = it.isExhibitionActive
                            it.items?.let {products ->
                                productsList = products
                                searchProduct(products)
                            }
                        }
                    )
                }
            }
            getBasketCount()
        }
    }
    //---------------------------------------------------------------------------------------------- getBasket



    //---------------------------------------------------------------------------------------------- setFilterByProductName
    fun setFilterByProductName(search: String, type: EnumProductPageType) {
        productSearch = search
        getBasket(type = type)
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




    //---------------------------------------------------------------------------------------------- requestAddToReturn
    fun requestAddToReturn(request: AddToBasket) {
        viewModelScope.launch(IO + exceptionHandler()){
            callApi(
                request = basketRepository.requestAddToReturnBasket(request),
                onReceiveData = {
                    addToBasketLiveData.postValue(it)
                    changeCount(request.ProductId, request.Count)
                }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestAddToReturn


    //---------------------------------------------------------------------------------------------- changeCount
    private fun changeCount(productId: Int, count: Int) {
        val item = productsList?.find {
            it.productId == productId
        }
        item?.count = count
    }
    //---------------------------------------------------------------------------------------------- changeCount



    //---------------------------------------------------------------------------------------------- requestDeleteBasket
    fun requestDeleteBasket(productId: Int, type: EnumProductPageType) {
        viewModelScope.launch(IO + exceptionHandler()){
            when(type) {
                EnumProductPageType.Product -> callApi(
                    request = basketRepository.requestDeleteBasket(productId),
                    onReceiveData = {
                        productsList = null
                        getBasket(type = type)
                        getBasketCount()
                    }
                )
                EnumProductPageType.Return -> callApi(
                    request = basketRepository.requestDeleteReturnBasket(productId),
                    onReceiveData = {
                        productsList = null
                        getBasket(type = type)
                        getBasketCount()
                    }
                )
            }
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
    fun requestSubmitBasket(description: String, exhibition: Boolean, type : EnumProductPageType) {
        viewModelScope.launch(IO + exceptionHandler()) {
            when(type){
                EnumProductPageType.Product -> callApi(
                    request = basketRepository.requestSubmitBasket(
                        description = description,
                        exhibition = exhibition
                    ),
                    showMessageAfterSuccessResponse = true,
                    onReceiveData = { submitBasketLiveData.postValue(true) }
                )
                EnumProductPageType.Return -> callApi(
                    request = basketRepository.requestSubmitReturnBasket(
                        description = description,
                        exhibition = exhibition
                    ),
                    showMessageAfterSuccessResponse = true,
                    onReceiveData = { submitBasketLiveData.postValue(true) }
                )
            }

        }
    }
    //---------------------------------------------------------------------------------------------- requestSubmitBasket



    //---------------------------------------------------------------------------------------------- requestDeleteBasket
    fun requestDeleteBasket(type : EnumProductPageType) {
        viewModelScope.launch(IO + exceptionHandler()) {
            when(type){
                EnumProductPageType.Product -> callApi(
                    request = basketRepository.requestDeleteAllBasket(),
                    showMessageAfterSuccessResponse = true,
                    onReceiveData = { submitBasketLiveData.postValue(true) }
                )
                EnumProductPageType.Return -> callApi(
                    request = basketRepository.requestDeleteReturnAllBasket(),
                    showMessageAfterSuccessResponse = true,
                    onReceiveData = { submitBasketLiveData.postValue(true) }
                )
            }

        }
    }
    //---------------------------------------------------------------------------------------------- requestDeleteBasket


}