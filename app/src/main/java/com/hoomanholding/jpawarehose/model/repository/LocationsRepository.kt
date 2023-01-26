package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.api.Api
import com.hoomanholding.jpawarehose.model.database.dao.LocationDao
import com.hoomanholding.jpawarehose.model.database.entity.LocationEntity
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