package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.api.Api
import com.hoomanholding.jpawarehose.model.database.dao.ReceiptDao
import com.hoomanholding.jpawarehose.model.database.dao.ReceiptDetailDao
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptDetailEntity
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptEntity
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/22/2023
 */
class ReceiptRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository,
    private val receiptDao: ReceiptDao,
    private val receiptDetailDao: ReceiptDetailDao
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


    //---------------------------------------------------------------------------------------------- requestGerReceiptDetail
    suspend fun requestGerReceiptDetail(id : Long) =
        apiCall { api.requestGetReceiptDetail(id, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGerReceiptDetail


    //---------------------------------------------------------------------------------------------- insertReceiptDetail
    fun insertReceiptDetail(details : List<ReceiptDetailEntity>) {
        receiptDetailDao.insertReceiptDetail(details)
    }
    //---------------------------------------------------------------------------------------------- insertReceiptDetail


    //---------------------------------------------------------------------------------------------- getReceiptDetail
    fun getReceiptDetail() = receiptDetailDao.getReceiptDetail()
    //---------------------------------------------------------------------------------------------- getReceiptDetail


    //---------------------------------------------------------------------------------------------- getReceiptDetailJoin
    fun getReceiptDetailJoin() = receiptDetailDao.getReceiptDetailJoin()
    //---------------------------------------------------------------------------------------------- getReceiptDetailJoin


    //---------------------------------------------------------------------------------------------- getReceipt
    fun getReceipt(id : Long) = receiptDao.getReceipt(id)
    //---------------------------------------------------------------------------------------------- getReceipt
}