package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject


/**
 * create by m-latifi on 4/5/2023
 */

class CustomerRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {

    //---------------------------------------------------------------------------------------------- requestGetCustomerState
    suspend fun requestGetCustomerState() =
        apiCall { api.requestGetCustomerState(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetCustomerState



    //---------------------------------------------------------------------------------------------- requestEditCustomerLocation
    suspend fun requestEditCustomerLocation(lat: Double, long: Double) =
        apiCall { api.requestEditCustomerLocation(lat,long, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestEditCustomerLocation

}