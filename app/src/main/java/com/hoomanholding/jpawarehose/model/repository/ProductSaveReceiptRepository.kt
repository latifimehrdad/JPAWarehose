package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.database.dao.ProductDao
import com.hoomanholding.jpawarehose.model.database.dao.ProductSaveReceiptDao
import com.hoomanholding.jpawarehose.model.database.entity.ProductSaveReceiptEntity
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/27/2023
 */
class ProductSaveReceiptRepository @Inject constructor(
    private val productSaveReceiptDao: ProductSaveReceiptDao,
    private val productDao: ProductDao
) {

    //---------------------------------------------------------------------------------------------- getProductForSaveReceiptByIgnoreBrandId
    fun getProductForSaveReceiptByIgnoreBrandId(ignoreBrandId : Long)
    : List<ProductSaveReceiptEntity> {
        val items = productSaveReceiptDao.getProductSaveReceipts()
        items.ifEmpty {
            val product = productDao.getProductByIgnoreBrandId(ignoreBrandId)
            val inserts = product.map {
                ProductSaveReceiptEntity(
                    it, 0, 0
                )
            }
            productSaveReceiptDao.insertProductSaveReceipts(inserts)
            productSaveReceiptDao.getProductSaveReceipts()
        }
        return items
    }
    //---------------------------------------------------------------------------------------------- getProductForSaveReceiptByIgnoreBrandId


    //---------------------------------------------------------------------------------------------- updateProduct
    fun updateProduct(productSaveReceiptEntity : ProductSaveReceiptEntity) {
        productSaveReceiptDao.updateProduct(productSaveReceiptEntity)
    }
    //---------------------------------------------------------------------------------------------- updateProduct

}