package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject


/**
 * create by m-latifi on 3/18/2023
 */

class OrderRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {

    //---------------------------------------------------------------------------------------------- requestCustomerGetOrder
    suspend fun requestCustomerGetOrder() =
        apiCall { api.requestCustomerGetOrder(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestCustomerGetOrder



    //---------------------------------------------------------------------------------------------- requestGetCustomerOrderDetail
    suspend fun requestGetCustomerOrderDetail(orderId: Long) =
        apiCall { api.requestGetCustomerOrderDetail(orderId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetCustomerOrderDetail

}