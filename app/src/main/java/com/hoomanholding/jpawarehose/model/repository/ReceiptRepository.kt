package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.api.Api
import com.hoomanholding.jpawarehose.model.database.dao.ReceiptDao
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptEntity
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/22/2023
 */
class ReceiptRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository,
    private val receiptDao: ReceiptDao
    ) {


    //---------------------------------------------------------------------------------------------- requestGetReceipts
    suspend fun requestGetReceipts() =
        apiCall { api.requestGetReceipts(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetReceipts


    //---------------------------------------------------------------------------------------------- insertReceipts
    fun insertReceipts(receipts : List<ReceiptEntity>) {
        receiptDao.insertReceipt(receipts)
    }
    //---------------------------------------------------------------------------------------------- insertReceipts

}