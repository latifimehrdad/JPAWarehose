package com.hoomanholding.jpawarehose.model.repository

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hoomanholding.jpawarehose.model.data.database.dao.ProductDao
import com.hoomanholding.jpawarehose.model.data.database.dao.SaveReceiptAmountDao
import com.hoomanholding.jpawarehose.model.data.database.entity.SaveReceiptAmountEntity
import com.hoomanholding.jpawarehose.model.data.database.join.ProductAmountSearchModel
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/27/2023
 */

class SaveReceiptAmountRepository @Inject constructor(
    private val saveReceiptAmountDao: SaveReceiptAmountDao,
    private val productDao: ProductDao
) {

    //---------------------------------------------------------------------------------------------- deleteAll
    fun deleteAll() {
        saveReceiptAmountDao.deleteAllRecord()
    }
    //---------------------------------------------------------------------------------------------- deleteAll


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
        Log.e("meri", query.sql)
        return productDao.search(query)
    }
    //---------------------------------------------------------------------------------------------- search


}