package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.api.Api
import com.hoomanholding.jpawarehose.model.data.request.AddWarehouseReceipt
import com.hoomanholding.jpawarehose.model.database.dao.SaveReceiptDao
import com.hoomanholding.jpawarehose.model.database.entity.SaveReceiptEntity
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/27/2023
 */
class SaveReceiptRepository @Inject constructor(
    private val api: Api,
    private val saveReceiptDao : SaveReceiptDao,
    private val tokenRepository: TokenRepository
) {

    //---------------------------------------------------------------------------------------------- insertSaveReceipts
    fun insertSaveReceipts(saveReceipt: SaveReceiptEntity) {
        saveReceiptDao.insertSaveReceipts(saveReceipt)
    }
    //---------------------------------------------------------------------------------------------- insertSaveReceipts


    //---------------------------------------------------------------------------------------------- getSaveReceipt
    fun getSaveReceipt() = saveReceiptDao.getSaveReceipts()
    //---------------------------------------------------------------------------------------------- getSaveReceipt


    //---------------------------------------------------------------------------------------------- updateReceiptNumber
    fun updateReceiptNumber(number : Long) {
        saveReceiptDao.updateReceiptNumber(number)
    }
    //---------------------------------------------------------------------------------------------- updateReceiptNumber


    //---------------------------------------------------------------------------------------------- deleteAllRecord
    fun deleteAllRecord() {
        saveReceiptDao.deleteAllRecord()
    }
    //---------------------------------------------------------------------------------------------- deleteAllRecord


    //---------------------------------------------------------------------------------------------- getCount
    fun getCount() = saveReceiptDao.getCount()
    //---------------------------------------------------------------------------------------------- getCount


    //---------------------------------------------------------------------------------------------- requestAddWarehouseReceipt
    suspend fun requestAddWarehouseReceipt(request : AddWarehouseReceipt) =
        apiCall { api.requestAddWarehouseReceipt(request, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestAddWarehouseReceipt

}