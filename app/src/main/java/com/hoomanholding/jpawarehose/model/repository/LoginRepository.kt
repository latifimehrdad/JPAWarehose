package com.hoomanholding.jpawarehose.model.repository

import com.hoomanholding.jpawarehose.model.api.ApiSuperApp
import com.hoomanholding.jpawarehose.model.data.request.LoginRequestModel
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Created by m-latifi on 11/9/2022.
 */

class LoginRepository @Inject constructor(private val api: ApiSuperApp) {

    //---------------------------------------------------------------------------------------------- requestLogin
    suspend fun requestLogin(login : LoginRequestModel) =
        apiCall{ api.requestLogin(login) }
    //---------------------------------------------------------------------------------------------- requestLogin


}