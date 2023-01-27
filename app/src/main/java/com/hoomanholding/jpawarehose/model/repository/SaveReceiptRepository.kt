package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.database.dao.SaveReceiptDao
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/27/2023
 */
class SaveReceiptRepository @Inject constructor(
    private val saveReceiptDao : SaveReceiptDao
) {

    //---------------------------------------------------------------------------------------------- getSaveReceipt
    fun getSaveReceipt() = saveReceiptDao.getSaveReceipts()
    //---------------------------------------------------------------------------------------------- getSaveReceipt

}