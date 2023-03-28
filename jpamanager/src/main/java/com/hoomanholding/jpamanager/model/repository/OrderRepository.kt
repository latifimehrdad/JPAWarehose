package com.hoomanholding.jpamanager.model.repository

import com.hoomanholding.jpamanager.model.api.Api
import com.hoomanholding.jpamanager.model.data.request.OrderRequestModel
import com.zar.core.tools.api.apiCall
import javax.inject.Inject


/**
 * create by m-latifi on 3/18/2023
 */

class OrderRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {


    //---------------------------------------------------------------------------------------------- requestGetOrder
    suspend fun requestGetOrder(request: OrderRequestModel) =
        apiCall { api.requestGetOrder(request, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetOrder


    //---------------------------------------------------------------------------------------------- requestOrderDetail
    suspend fun requestOrderDetail(orderId: Long) =
        apiCall { api.requestOrderDetail(orderId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestOrderDetail

}