package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.api.Api
import com.hoomanholding.jpawarehose.model.database.dao.ProductDao
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
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


    //---------------------------------------------------------------------------------------------- getProductByIgnoreBrandId
    fun getProductByIgnoreBrandId(ignoreBrandId : Long) =
        productDao.getProductByIgnoreBrandId(ignoreBrandId)
    //---------------------------------------------------------------------------------------------- getProductByIgnoreBrandId

}