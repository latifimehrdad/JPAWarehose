package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.database.dao.LocationDao
import com.hoomanholding.applibrary.model.data.database.entity.LocationEntity
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */
class LocationsRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository,
    private val locationDao: LocationDao
) {


    //---------------------------------------------------------------------------------------------- requestGetLocations
    suspend fun requestGetLocations() =
        apiCall{ api.requestGetLocations(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetLocations


    //---------------------------------------------------------------------------------------------- insertLocations
    fun insertLocations(locations: List<LocationEntity>) {
        locationDao.insertLocation(locations)
    }
    //---------------------------------------------------------------------------------------------- insertLocations


    //---------------------------------------------------------------------------------------------- deleteAllLocations
    fun deleteAllLocations() {
        locationDao.deleteAll()
    }
    //---------------------------------------------------------------------------------------------- deleteAllLocations
}