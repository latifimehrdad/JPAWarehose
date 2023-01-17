package com.hoomanholding.jpawarehose.model.api

import com.hoomanholding.jpawarehose.model.data.request.LoginRequestModel
import com.hoomanholding.jpawarehose.model.response.LoginResponseModel
import retrofit2.Response
import retrofit2.http.*


/**
 * Created by m-latifi on 11/26/2022.
 */

interface ApiSuperApp {

    companion object {
        const val api = "/Api"
        const val v1 = "$api/V1"
    }

    @POST("$v1/LogIn/login-users")
    suspend fun requestLogin(@Body login : LoginRequestModel) : Response<LoginResponseModel>



}