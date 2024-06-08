package com.zarholding.jpacustomer.view.fragment.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.skydoves.powerspinner.IconSpinnerItem
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
    private var productSearch: String = ""
    private var sortType: SortType? = null
    val productNewLiveData = MutableLiveData(false)
    val productLiveData: MutableLiveData<List<ProductModel>> by lazy {
        MutableLiveData<List<ProductModel>>()
    }
    val basketCountLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }


    enum class SortType(val persianName: String, val index: Int) {
        MaxPrice("بیشترین قیمت", 0),
        MinPrice("کمترین قیمت", 1),
        BestSales("بیشترین فروش", 2),
        LowestSales("کمترین فروش", 3),
        ProductName("نام کالا", 4),
        ProductCode("کد کالا", 5)
    }


    //---------------------------------------------------------------------------------------------- getItemsSort
    fun getItemsSort(): List<IconSpinnerItem> {
        val list = mutableListOf<IconSpinnerItem>()
        for (name in SortType.values()) {
            list.add(
                IconSpinnerItem(name.persianName)
            )
        }
        return list
    }
    //---------------------------------------------------------------------------------------------- getItemsSort


    //---------------------------------------------------------------------------------------------- getProduct
    fun getProduct() {
        viewModelScope.launch(IO + exceptionHandler()) {
            productsList?.let {
                searchProduct(it)
            } ?: run {
                callApi(
                    request = productRepository.requestGetCustomerProducts(),
                    onReceiveData = { products ->
                        sortType = null
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
        val list = sortList(items = filterProductList(items = items))
        productLiveData.postValue(list)
    }
    //---------------------------------------------------------------------------------------------- searchProduct


    //---------------------------------------------------------------------------------------------- filterProductList
    private fun filterProductList(items: List<ProductModel>): List<ProductModel> {
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
        return list
    }
    //---------------------------------------------------------------------------------------------- filterProductList


    //---------------------------------------------------------------------------------------------- sortList
    private fun sortList(items: List<ProductModel>) =
        if (sortType == null)
            items
        else
            when (sortType!!) {
                SortType.MaxPrice -> {
                    items.sortedByDescending { model ->
                        model.price
                    }
                }

                SortType.MinPrice -> {
                    items.sortedBy { model ->
                        model.price
                    }
                }

                SortType.BestSales -> {
                    items.sortedByDescending { model ->
                        model.price
                    }
                }

                SortType.LowestSales -> {
                    items.sortedBy { model ->
                        model.price
                    }
                }

                SortType.ProductCode -> {
                    items.sortedBy { model ->
                        model.productCode
                    }
                }

                SortType.ProductName -> {
                    items.sortedBy { model ->
                        model.productName
                    }
                }
            }
    //---------------------------------------------------------------------------------------------- sortList


    //---------------------------------------------------------------------------------------------- findWordInProductName
    private fun findWordInProductName(words: List<String>, product: ProductModel): Boolean {
        val text = "${product.productCode} ${product.productName}"
        for (word in words) {
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


    //---------------------------------------------------------------------------------------------- getSortIndex
    fun getSortIndex() = sortType?.index ?: -1
    //---------------------------------------------------------------------------------------------- getSortIndex


    //---------------------------------------------------------------------------------------------- setSortIndex
    fun setSortIndex(index: Int) {
        sortType = SortType.values().find {
            it.index == index
        }
        getProduct()
    }
    //---------------------------------------------------------------------------------------------- setSortIndex

}