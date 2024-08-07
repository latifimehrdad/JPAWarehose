package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.model.data.request.LoginRequestModel
import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.request.ForgetPassModel
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Created by m-latifi on 11/9/2022.
 */

class LoginRepository @Inject constructor(private val api: Api){

    //---------------------------------------------------------------------------------------------- requestLogin
    suspend fun requestLogin(login : LoginRequestModel) =
        apiCall{ api.requestLogin(login) }
    //---------------------------------------------------------------------------------------------- requestLogin



    //---------------------------------------------------------------------------------------------- requestForgetPassword
    suspend fun requestForgetPassword(forgetPassModel: ForgetPassModel) =
        apiCall { api.requestForgetPassword(
            systemTypesEnum = forgetPassModel.systemTypesEnum.name,
            userName = forgetPassModel.userName,
            androidId = forgetPassModel.androidId
        ) }
    //---------------------------------------------------------------------------------------------- requestForgetPassword

}