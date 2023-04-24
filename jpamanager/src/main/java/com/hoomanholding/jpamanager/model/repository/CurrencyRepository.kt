package com.hoomanholding.jpamanager.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject


/**
 * create by m-latifi on 4/24/2023
 */

class CurrencyRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {

    //---------------------------------------------------------------------------------------------- requestGetCurrency
    suspend fun requestGetCurrency() =
        apiCall { api.requestGetCurrency(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetCurrency

}