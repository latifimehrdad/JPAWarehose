package com.hoomanholding.jpawarehose.model.repository.receipt

import androidx.sqlite.db.SimpleSQLiteQuery
import com.hoomanholding.jpawarehose.model.api.Api
import com.hoomanholding.jpawarehose.model.data.database.dao.ProductDao
import com.hoomanholding.jpawarehose.model.data.database.dao.receipt.history.HistorySaveReceiptAmountDao
import com.hoomanholding.jpawarehose.model.data.database.dao.receipt.history.HistorySaveReceiptDao
import com.hoomanholding.jpawarehose.model.data.database.dao.receipt.save.SaveReceiptAmountDao
import com.hoomanholding.jpawarehose.model.data.database.dao.receipt.save.SaveReceiptDao
import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.history.HistorySaveReceiptAmountEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.history.HistorySaveReceiptEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.save.SaveReceiptAmountEntity
import com.hoomanholding.jpawarehose.model.data.database.entity.receipt.save.SaveReceiptEntity
import com.hoomanholding.jpawarehose.model.data.database.join.ProductAmountSearchModel
import com.hoomanholding.jpawarehose.model.data.request.AddWarehouseReceipt
import com.hoomanholding.jpawarehose.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/27/2023
 */
class SaveReceiptRepository @Inject constructor(
    private val api: Api,
    private val saveReceiptDao: SaveReceiptDao,
    private val tokenRepository: TokenRepository,
    private val saveReceiptAmountDao: SaveReceiptAmountDao,
    private val productDao: ProductDao,
    private val historySaveReceiptDao: HistorySaveReceiptDao,
    private val historySaveReceiptAmountDao: HistorySaveReceiptAmountDao
) {

    //---------------------------------------------------------------------------------------------- insertSaveReceipts
    fun insertSaveReceipts(saveReceipt: SaveReceiptEntity) {
        saveReceiptDao.insertSaveReceipts(saveReceipt)
    }
    //---------------------------------------------------------------------------------------------- insertSaveReceipts


    //---------------------------------------------------------------------------------------------- getSaveReceipt
    fun getSaveReceipt() = saveReceiptDao.getSaveReceipt()
    //---------------------------------------------------------------------------------------------- getSaveReceipt


    //---------------------------------------------------------------------------------------------- getHistorySaveReceipt
    fun getHistorySaveReceipt() = historySaveReceiptDao.getSaveReceipt()
    //---------------------------------------------------------------------------------------------- getHistorySaveReceipt


    //---------------------------------------------------------------------------------------------- updateReceiptNumber
    fun updateReceiptNumber(number: Long) {
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
    suspend fun requestAddWarehouseReceipt(request: AddWarehouseReceipt) =
        apiCall { api.requestAddWarehouseReceipt(request, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestAddWarehouseReceipt


    //---------------------------------------------------------------------------------------------- deleteAllAmount
    fun deleteAllAmount() {
        saveReceiptAmountDao.deleteAllRecord()
    }
    //---------------------------------------------------------------------------------------------- deleteAllAmount


    //---------------------------------------------------------------------------------------------- insertSaveReceiptAmount
    fun insertSaveReceiptAmount(amount: SaveReceiptAmountEntity) {
        saveReceiptAmountDao.insertSaveReceiptAmount(amount)
    }
    //---------------------------------------------------------------------------------------------- insertSaveReceiptAmount


    //---------------------------------------------------------------------------------------------- getReceiptAmountWithProduct
    fun getReceiptAmountWithProduct() = saveReceiptAmountDao.getReceiptAmountWithProduct()
    //---------------------------------------------------------------------------------------------- getReceiptAmountWithProduct


    //---------------------------------------------------------------------------------------------- search
    fun search(
        ignoreBrandId: Long,
        orderBy: String,
        orderType: String,
        words: List<String>? = null
    ): List<ProductAmountSearchModel> {
        val selectQuery = "SELECT * FROM Product WHERE brandId != $ignoreBrandId"
        var finalQuery = selectQuery
        if (words != null)
            finalQuery = selectQuery + words.joinToString(prefix = " AND ", separator = " AND ") {
                "(codeKala LIKE '%${it.persianNumberToEnglishNumber()}%' OR nameKala LIKE '%${it.persianNumberToEnglishNumber()}%')"
            }
        finalQuery = "$finalQuery ORDER BY $orderBy $orderType"
        val query = SimpleSQLiteQuery(finalQuery)
        return productDao.search(query)
    }
    //---------------------------------------------------------------------------------------------- search


    //---------------------------------------------------------------------------------------------- insertHistorySaveReceipt
    fun insertHistorySaveReceipt(historySaveReceiptEntity : HistorySaveReceiptEntity) =
        historySaveReceiptDao.insertSaveReceipts(historySaveReceiptEntity)
    //---------------------------------------------------------------------------------------------- insertHistorySaveReceipt


    //---------------------------------------------------------------------------------------------- insertHistorySaveReceiptAmount
    fun insertHistorySaveReceiptAmount(amounts: List<HistorySaveReceiptAmountEntity>) {
        historySaveReceiptAmountDao.insertSaveReceiptAmount(amounts)
    }
    //---------------------------------------------------------------------------------------------- insertHistorySaveReceiptAmount

}