package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject


/**
 * create by m-latifi on 4/25/2023
 */

class AppUpdateRepository @Inject constructor(
    private val api: Api
) {

    //---------------------------------------------------------------------------------------------- requestGetAppVersion
    suspend fun requestGetAppVersion(appName: String) =
        apiCall { api.requestGetAppVersion(appName) }
    //---------------------------------------------------------------------------------------------- requestGetAppVersion

}