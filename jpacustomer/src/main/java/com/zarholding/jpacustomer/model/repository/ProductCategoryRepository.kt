package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */
class ProductCategoryRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {

    //---------------------------------------------------------------------------------------------- requestGetProductCategory
    suspend fun requestGetProductCategory(lvl: Int) =
        apiCall { api.requestGetProductCategory(
                lvl = lvl,
                token = tokenRepository.getBearerToken()
        ) }
    //---------------------------------------------------------------------------------------------- requestGetProductCategory

}