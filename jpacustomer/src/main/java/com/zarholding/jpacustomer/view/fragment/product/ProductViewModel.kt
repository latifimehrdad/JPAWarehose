package com.zarholding.jpacustomer.view.fragment.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.category.CategoryModel
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.skydoves.powerspinner.IconSpinnerItem
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import com.zarholding.jpacustomer.model.repository.BasketRepository
import com.zarholding.jpacustomer.model.repository.ProductCategoryRepository
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
        private val basketRepository: BasketRepository,
        private val productCategoryRepository: ProductCategoryRepository
) : JpaViewModel() {

    private var productsList: List<ProductModel>? = null
    private var productSearch: String = ""
    private var sortType: SortType? = null
    val productNewLiveData = MutableLiveData(false)
    val productLiveData: MutableLiveData<List<ProductModel>> by lazy {
        MutableLiveData<List<ProductModel>>()
    }
    val basketCountLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    private var categoryLevel: CategoryLevel? = null
    private var categoryIndex : Int = -1
    private var categoryList: List<CategoryModel> = emptyList()
    val categoryLiveData: MutableLiveData<List<CategoryModel>> by lazy {
        MutableLiveData<List<CategoryModel>>()
    }


    enum class CategoryLevel(val persianName: String, val index: Int, val level: Int) {
        MaxPrice("سطح 3", 0, 3),
        MinPrice("سطح 4", 1, 4)
    }


    enum class SortType(val persianName: String, val index: Int) {
        MaxPrice("بیشترین قیمت", 0),
        MinPrice("کمترین قیمت", 1),
//        BestSales("بیشترین فروش", 2),
//        LowestSales("کمترین فروش", 3),
        ProductName("نام کالا", 2),
        ProductCode("کد کالا", 3)
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


    //---------------------------------------------------------------------------------------------- getCategoryLevel
    fun getCategoryLevel(): List<IconSpinnerItem> {
        val list = mutableListOf<IconSpinnerItem>()
        for (name in CategoryLevel.values()) {
            list.add(
                    IconSpinnerItem(name.persianName)
            )
        }
        return list
    }
    //---------------------------------------------------------------------------------------------- getCategoryLevel


    //---------------------------------------------------------------------------------------------- getProduct
    private fun getCategory() {
        viewModelScope.launch(IO + exceptionHandler()) {
            categoryLevel?.let { cat ->
                callApi(
                        request = productCategoryRepository.requestGetProductCategory(
                                lvl = cat.level
                        ),
                        onReceiveData = {
                            categoryList = it
                            categoryLiveData.postValue(categoryList)
                            searchProduct(productsList?: emptyList())
                        }
                )
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getProduct


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
        val list = sortList(items = filterByCategory(items = filterProductList(items = items)))
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


    //---------------------------------------------------------------------------------------------- filterByCategory
    private fun filterByCategory(items: List<ProductModel>) : List<ProductModel> {
            if (categoryLevel == null || categoryIndex == -1 || categoryList.size < categoryIndex + 1)
                return items
            else {
                val category = categoryList[categoryIndex]
                return when(categoryLevel!!.level) {
                    3 -> {
                        items.filter {product ->
                            product.productCategoryLevel3 == category.productCategoryCode
                        }
                    }

                    4 -> {
                        items.filter {product ->
                            product.productCategoryLevel4 == category.productCategoryCode
                        }
                    }

                    else -> {
                        items
                    }
                }
            }
        }
    //---------------------------------------------------------------------------------------------- filterByCategory


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


    //---------------------------------------------------------------------------------------------- getCategoryLevelIndex
    fun getCategoryLevelIndex() = categoryLevel?.index ?: -1
    //---------------------------------------------------------------------------------------------- getCategoryLevelIndex


    //---------------------------------------------------------------------------------------------- getCategoryLevelIndex
    fun getCategoryIndex() = categoryIndex
    //---------------------------------------------------------------------------------------------- getCategoryLevelIndex


    //---------------------------------------------------------------------------------------------- setSortIndex
    fun setSortIndex(index: Int) {
        sortType = SortType.values().find {
            it.index == index
        }
        getProduct()
    }
    //---------------------------------------------------------------------------------------------- setSortIndex


    //---------------------------------------------------------------------------------------------- setSortIndex
    fun setCategoryLevel(index: Int) {
        if (index != categoryLevel?.index)
            categoryIndex = -1
        categoryLevel = CategoryLevel.values().find {
            it.index == index
        }
        getCategory()
    }
    //---------------------------------------------------------------------------------------------- setSortIndex


    //---------------------------------------------------------------------------------------------- setSortIndex
    fun setCategoryIndex(index: Int) {
        categoryIndex = index
        getProduct()
    }
    //---------------------------------------------------------------------------------------------- setSortIndex

}