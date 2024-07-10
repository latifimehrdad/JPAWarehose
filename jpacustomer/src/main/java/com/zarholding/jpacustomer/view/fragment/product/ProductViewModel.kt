package com.zarholding.jpacustomer.view.fragment.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.category.CategoryModel
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.skydoves.powerspinner.IconSpinnerItem
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import com.zarholding.jpacustomer.model.EnumProductPageType
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

    private var productPageType: EnumProductPageType = EnumProductPageType.Product
    private var productsList: List<ProductModel>? = null
    private var productSearch: String = ""
    private var sortType: SortType? = SortType.ProductName
    val productTypeLiveData = MutableLiveData(ProductType.All)
    val productLiveData: MutableLiveData<List<ProductModel>> by lazy {
        MutableLiveData<List<ProductModel>>()
    }
    val basketCountLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    private var categoryLevel: CategoryLevel? = CategoryLevel.All
    private var categoryIndex: Int = CategoryLevel.All.index
    private var categoryList: List<CategoryModel> = emptyList()
    val categoryLiveData: MutableLiveData<List<CategoryModel>> by lazy {
        MutableLiveData<List<CategoryModel>>()
    }

    enum class ProductType {
        All,
        New,
        Special
    }

    enum class CategoryLevel(val persianName: String, val index: Int, val level: Int) {
        MaxPrice("گروه قطعات", 0, 3),
        MinPrice("نوع خودرو", 1, 4),
        All("همه", 2, 0)
    }


    enum class SortType(val persianName: String, val index: Int) {
        ProductName("نام کالا", 0),
        ProductCode("کد کالا", 1),
        Discount("شگفت انگیز نمایشگاه", 2),
        MaxPrice("بیشترین قیمت", 3),
        MinPrice("کمترین قیمت", 4)
    }


    //---------------------------------------------------------------------------------------------- clearFilter
    fun clearFilter(type: EnumProductPageType) {
        viewModelScope.launch {
            categoryLiveData.postValue(emptyList())
            productTypeLiveData.postValue(ProductType.All)
            delay(500)
            sortType = SortType.ProductName
            categoryLevel = CategoryLevel.All
            categoryIndex = CategoryLevel.All.index
            if (productSearch.isEmpty())
                getProduct(type = type)
        }
    }
    //---------------------------------------------------------------------------------------------- clearFilter



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
                if (cat.index != CategoryLevel.All.index)
                    callApi(
                        request = productCategoryRepository.requestGetProductCategory(
                            lvl = cat.level
                        ),
                        onReceiveData = {
                            categoryList = it
                            categoryLiveData.postValue(categoryList)
                            if (!productsList.isNullOrEmpty())
                             searchProduct(productsList!!)
                        }
                    )
                else {
                    categoryLiveData.postValue(emptyList())
                    if (!productsList.isNullOrEmpty())
                        searchProduct(productsList!!)
                }

            }
        }
    }
    //---------------------------------------------------------------------------------------------- getProduct


    //---------------------------------------------------------------------------------------------- getProduct
    fun getProduct(type: EnumProductPageType) {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (type == productPageType && productsList != null)
                searchProduct(productsList!!)
            else {
                productPageType = type
                when (type) {
                    EnumProductPageType.Product -> callApi(
                        request = productRepository.requestGetCustomerProducts(),
                        onReceiveData = { products ->
                            sortType = SortType.ProductName
                            productsList = products
                            searchProduct(products)
                        }
                    )

                    EnumProductPageType.Return -> callApi(
                        request = productRepository.requestGetCustomerReturnProducts(),
                        onReceiveData = { products ->
                            sortType = SortType.ProductName
                            productsList = products
                            searchProduct(products)
                        }
                    )
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getProduct


    //---------------------------------------------------------------------------------------------- setFilterByProductNew
    fun setFilterByProductNew(type: ProductType) {
        viewModelScope.launch(IO + exceptionHandler()) {
            productTypeLiveData.postValue(type)
            delay(500)
            getProduct(type = productPageType)
        }
    }
    //---------------------------------------------------------------------------------------------- setFilterByProductNew


    //---------------------------------------------------------------------------------------------- setFilterByProductName
    fun setFilterByProductName(search: String, type: EnumProductPageType) {
        productSearch = search
        getProduct(type = type)
    }
    //---------------------------------------------------------------------------------------------- setFilterByProductName


    //---------------------------------------------------------------------------------------------- searchProduct
    private fun searchProduct(items: List<ProductModel>) {
        val list = sortList(items = filterByCategory(items = filterByType(items = filterProductList(items = items))))
        productLiveData.postValue(list)
    }
    //---------------------------------------------------------------------------------------------- searchProduct


    //---------------------------------------------------------------------------------------------- filterProductList
    private fun filterProductList(items: List<ProductModel>): List<ProductModel> {
        productSearch = productSearch.persianNumberToEnglishNumber()
        val words = productSearch.split(" ")
        val list = items.filter { product ->
            findWordInProductName(words, product)
        }
        return list
    }
    //---------------------------------------------------------------------------------------------- filterProductList


    //---------------------------------------------------------------------------------------------- filterByCategory
    private fun filterByType(items: List<ProductModel>): List<ProductModel> {
        return when (productTypeLiveData.value) {
            ProductType.New -> {
                items.filter { product ->
                    product.isNew
                }
            }

            ProductType.Special -> {
                items.filter { product ->
                    product.isSpecial
                }
            }
            else ->{
                items
            }

        }
    }
    //---------------------------------------------------------------------------------------------- filterByCategory



    //---------------------------------------------------------------------------------------------- filterByCategory
    private fun filterByCategory(items: List<ProductModel>): List<ProductModel> {
        if (categoryLevel == null || categoryIndex == -1 || categoryList.size < categoryIndex + 1)
            return items
        else {
            val category = categoryList[categoryIndex]
            return when (categoryLevel!!.level) {
                3 -> {
                    items.filter { product ->
                        product.productCategoryLevel3 == category.productCategoryCode
                    }
                }

                4 -> {
                    items.filter { product ->
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

                SortType.Discount -> {
                    items.sortedByDescending {model ->
                        model.isSpecial
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
        getProduct(type = productPageType)
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
        getProduct(type = productPageType)
    }
    //---------------------------------------------------------------------------------------------- setSortIndex

}