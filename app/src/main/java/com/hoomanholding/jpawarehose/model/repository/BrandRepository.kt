package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.api.Api
import com.hoomanholding.jpawarehose.model.database.dao.BrandDao
import com.hoomanholding.jpawarehose.model.database.entity.BrandEntity
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/22/2023
 */
class BrandRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository,
    private val brandDao: BrandDao
) {


    //---------------------------------------------------------------------------------------------- requestGetBrands
    suspend fun requestGetBrands() =
        apiCall{ api.requestGetBrands(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetBrands


    //---------------------------------------------------------------------------------------------- getBrands
    fun getBrands() = brandDao.getBrands()
    //---------------------------------------------------------------------------------------------- getBrands


    //---------------------------------------------------------------------------------------------- insertBrands
    fun insertBrands(brands: List<BrandEntity>) {
        brandDao.insertBrands(brands)
    }
    //---------------------------------------------------------------------------------------------- insertBrands


    //---------------------------------------------------------------------------------------------- deleteAllBrands
    fun deleteAllBrands() {
        brandDao.deleteAll()
    }
    //---------------------------------------------------------------------------------------------- deleteAllBrands

}