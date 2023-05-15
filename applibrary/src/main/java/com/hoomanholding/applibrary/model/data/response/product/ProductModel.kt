package com.hoomanholding.applibrary.model.data.response.product

/**
 * Created by zar on 5/14/2023.
 */

data class ProductModel(
    val id: Int,
    val productCode: String?,
    val productName: String?,
    val productCountinBox: Int,
    val productCountinPackage: Int,
    val price: Long,
    val productCategoryLevel1: Int,
    val productCategoryLevel1Name: String?,
    val productCategoryLevel2: Int,
    val productCategoryLevel2Name: String?,
    val productCategoryLevel3: Int,
    val productCategoryLevel3Name: String?,
    val productCategoryLevel4: Int,
    val productCategoryLevel4Name: String?,
    val productBrandId: Int,
    val productBrandName: String?,
    val isNew: Boolean,
    val productThumbnailImageName: String?
)