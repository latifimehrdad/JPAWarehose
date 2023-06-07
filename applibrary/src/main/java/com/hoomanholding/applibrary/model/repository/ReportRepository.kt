package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject


/**
 * create by m-latifi on 4/19/2023
 */


class ReportRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {

    //---------------------------------------------------------------------------------------------- requestFirstPageReport
    suspend fun requestFirstPageReport(currencyTypeId: Int) =
        apiCall { api.requestFirstPageReport(currencyTypeId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestFirstPageReport


    //---------------------------------------------------------------------------------------------- requestVisitorSalesReport
    suspend fun requestVisitorSalesReport() =
        apiCall { api.requestVisitorSalesReport(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestVisitorSalesReport


    //---------------------------------------------------------------------------------------------- requestCustomerBalance
    suspend fun requestCustomerBalance() =
        apiCall { api.requestCustomerBalance(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestCustomerBalance


    //---------------------------------------------------------------------------------------------- requestCustomerBalanceDetail
    suspend fun requestCustomerBalanceDetail(customerId: Long) =
        apiCall { api.requestCustomerBalanceDetail(customerId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestCustomerBalanceDetail


    //---------------------------------------------------------------------------------------------- requestCustomersBouncedCheckReport
    suspend fun requestCustomersBouncedCheckReport() =
        apiCall { api.requestCustomersBouncedCheckReport(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestCustomersBouncedCheckReport
}