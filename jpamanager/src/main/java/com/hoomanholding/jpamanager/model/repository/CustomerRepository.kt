package com.hoomanholding.jpamanager.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.request.FilterCustomerRequest
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

    //---------------------------------------------------------------------------------------------- requestGetCustomer
    suspend fun requestGetCustomer(request: FilterCustomerRequest) =
        apiCall { api.requestGetCustomer(
            request.VisitorId,
            request.strFilter,
            tokenRepository.getBearerToken()
        ) }
    //---------------------------------------------------------------------------------------------- requestGetCustomer


    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial
    suspend fun requestGetCustomerFinancial(customerId: Int) =
        apiCall { api.requestGetCustomerFinancial(customerId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial


    // ---------------------------------------------------------------------------------------------- requestGetCustomerFinancial
    suspend fun requestGetCustomerFinancialDetail(customerId: Int, checkType: EnumCheckType) =
        apiCall { api.requestGetCustomerFinancialDetail(
            customerId,
            checkType,
            tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial


    //---------------------------------------------------------------------------------------------- requestGetAllCustomers
    suspend fun requestGetAllCustomers() =
        apiCall { api.requestGetAllCustomers(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetAllCustomers


    //---------------------------------------------------------------------------------------------- requestAddCustomerLicensing
    suspend fun requestAddCustomerLicensing(request: List<Long>) =
        apiCall { api.requestAddCustomerLicensing(request, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestAddCustomerLicensing

}