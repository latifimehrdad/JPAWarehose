package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.database.dao.ProductDao
import com.hoomanholding.applibrary.model.data.database.entity.ProductsEntity
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */
class ProductRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository,
    private val productDao: ProductDao
) {


    //---------------------------------------------------------------------------------------------- requestGetProducts
    suspend fun requestGetProducts() =
        apiCall{ api.requestGetProducts(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetProducts


    //---------------------------------------------------------------------------------------------- insertProducts
    fun insertProducts(products: List<ProductsEntity>) {
        productDao.insertProducts(products)
    }
    //---------------------------------------------------------------------------------------------- insertProducts


    //---------------------------------------------------------------------------------------------- deleteAllProduct
    fun deleteAllProduct() {
        productDao.deleteAll()
    }
    //---------------------------------------------------------------------------------------------- deleteAllProduct

}