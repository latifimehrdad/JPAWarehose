package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.api.Api
import com.hoomanholding.jpawarehose.model.data.database.dao.SupplierDao
import com.hoomanholding.jpawarehose.model.data.database.entity.SupplierEntity
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */
class SupplierRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository,
    private val supplierDao: SupplierDao
) {

    //---------------------------------------------------------------------------------------------- requestGetSuppliers
    suspend fun requestGetSuppliers() =
        apiCall{ api.requestGetSuppliers(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetSuppliers


    //---------------------------------------------------------------------------------------------- getSupplierFromDB
    fun getSupplierFromDB() = supplierDao.getSuppliers()
    //---------------------------------------------------------------------------------------------- getSupplierFromDB


    //---------------------------------------------------------------------------------------------- insertSuppliers
    fun insertSuppliers(supplies: List<SupplierEntity>) {
        supplierDao.insertSuppliers(supplies)
    }
    //---------------------------------------------------------------------------------------------- insertSuppliers


    //---------------------------------------------------------------------------------------------- deleteAllSuppliers
    fun deleteAllSuppliers() {
        supplierDao.deleteAll()
    }
    //---------------------------------------------------------------------------------------------- deleteAllSuppliers
}