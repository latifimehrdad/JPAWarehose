package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.response.GeneralResponse
import com.zar.core.tools.api.apiCall
import com.zar.core.tools.manager.DeviceManager
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by m-latifi on 7/1/2023.
 */

class LogRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository,
    private val deviceManager: DeviceManager
) {


    //---------------------------------------------------------------------------------------------- requestSaveError
    suspend fun requestSaveError(log: String): Response<GeneralResponse<Boolean?>>? {
        val fullLog = "${deviceManager.deviceBrand()} ${System.getProperty("line.separator")}" +
                "${deviceManager.androidVersion()} ${System.getProperty("line.separator")}" +
                "${deviceManager.deviceLanguage()} ${System.getProperty("line.separator")}" +
                "Log : $log"
        return apiCall { api.requestSaveError(fullLog, tokenRepository.getBearerToken()) }
    }
    //---------------------------------------------------------------------------------------------- requestSaveError

}