package com.hoomanholding.jpawarehose.model.repository.receipt

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.database.dao.LocationAmountDao
import com.hoomanholding.applibrary.model.data.database.dao.receipt.arrange.ReceiptDao
import com.hoomanholding.applibrary.model.data.database.dao.receipt.arrange.ReceiptDetailDao
import com.hoomanholding.applibrary.model.data.database.entity.LocationAmountEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptDetailEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptEntity
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/22/2023
 */
class ReceiptRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository,
    private val receiptDao: ReceiptDao,
    private val receiptDetailDao: ReceiptDetailDao,
    private val locationAmountDao: LocationAmountDao
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


    //---------------------------------------------------------------------------------------------- insertLocationAmount
    fun insertLocationAmount(locationAmount : LocationAmountEntity) {
        locationAmountDao.insertLocationAmount(locationAmount)
    }
    //---------------------------------------------------------------------------------------------- insertLocationAmount


    //---------------------------------------------------------------------------------------------- getSumAmount
    fun getSumAmount(locationId : Long, productId : Long) =
        locationAmountDao.getSumAmount(locationId, productId)
    //---------------------------------------------------------------------------------------------- getSumAmount


    //---------------------------------------------------------------------------------------------- getSumAmount
    fun getSumAmount(productId : Long) = locationAmountDao.getSumAmount(productId)
    //---------------------------------------------------------------------------------------------- getSumAmount


    //---------------------------------------------------------------------------------------------- requestConfirmReceipt
    suspend fun requestConfirmReceipt(receiptId : Long) =
        apiCall { api.requestConfirmReceipt(receiptId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestConfirmReceipt


    //---------------------------------------------------------------------------------------------- deleteAllReceiptDetailAndAmount
    fun deleteAllReceiptDetailAndAmount() {
        receiptDetailDao.deleteAllReceiptDetail()
        locationAmountDao.deleteAll()
    }
    //---------------------------------------------------------------------------------------------- deleteAllReceiptDetailAndAmount
}