package com.hoomanholding.jpawarehose.model.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.hoomanholding.jpawarehose.model.database.dao.ProductDao
import com.hoomanholding.jpawarehose.model.database.dao.ProductSaveReceiptDao
import com.hoomanholding.jpawarehose.model.database.entity.ProductSaveReceiptEntity
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/27/2023
 */

class ProductSaveReceiptRepository @Inject constructor(
    private val productSaveReceiptDao: ProductSaveReceiptDao,
    private val productDao: ProductDao
) {

    //---------------------------------------------------------------------------------------------- deleteAll
    fun deleteAll() {
        productSaveReceiptDao.deleteAllRecord()
    }
    //---------------------------------------------------------------------------------------------- deleteAll



    //---------------------------------------------------------------------------------------------- getProductFromDB
    fun getProductFromDB() = productSaveReceiptDao.getProductSaveReceipts()
    //---------------------------------------------------------------------------------------------- getProductFromDB



    //---------------------------------------------------------------------------------------------- getProductByIgnoreBrandId
    fun getProductByIgnoreBrandId(ignoreBrandId : Long)
    : List<ProductSaveReceiptEntity> {
        val product = productDao.getProductByIgnoreBrandId(ignoreBrandId)
        val inserts = product.map {
            ProductSaveReceiptEntity(
                it,
                "${it.productsEntity.codeKala} ${it.productsEntity.nameKala}",
                0, 0
            )
        }
        productSaveReceiptDao.insertProductSaveReceipts(inserts)
        return productSaveReceiptDao.getProductSaveReceipts()
    }
    //---------------------------------------------------------------------------------------------- getProductByIgnoreBrandId




    //---------------------------------------------------------------------------------------------- updateProduct
    fun updateProduct(productSaveReceiptEntity : ProductSaveReceiptEntity) {
        productSaveReceiptDao.updateProduct(productSaveReceiptEntity)
    }
    //---------------------------------------------------------------------------------------------- updateProduct


    //---------------------------------------------------------------------------------------------- search
    fun search(words : List<String>) : List<ProductSaveReceiptEntity> {
        val selectQuery = "SELECT * FROM ProductSaveReceipt"
        val finalQuery = selectQuery + words.joinToString(prefix = " WHERE ", separator = " AND ") {
            "search LIKE '%${it.persianNumberToEnglishNumber()}%'"
        }
        val query = SimpleSQLiteQuery(finalQuery)
        return productSaveReceiptDao.search(query)
    }
    //---------------------------------------------------------------------------------------------- search

}