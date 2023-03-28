package com.hoomanholding.jpamanager.model.api

import com.hoomanholding.jpamanager.model.data.request.LoginRequestModel
import com.hoomanholding.jpamanager.model.data.request.OrderRequestModel
import com.hoomanholding.jpamanager.model.data.response.order.DetailOrderResponseModel
import com.hoomanholding.jpamanager.model.data.response.order.OrderResponseModel
import com.hoomanholding.jpamanager.model.data.response.user.LoginResponseModel
import com.hoomanholding.jpamanager.model.data.response.user.UserInfoResponseModel
import com.hoomanholding.jpamanager.model.data.response.user.UserPermissionResponseModel
import retrofit2.Response
import retrofit2.http.*


/**
 * Created by m-latifi on 11/26/2022.
 */

interface Api {

    companion object {
        const val api = "/Api"
        const val v1 = "$api/V1"
    }

    @POST("$v1/User/login-users")
    suspend fun requestLogin(@Body login : LoginRequestModel) : Response<LoginResponseModel>


    @GET("${v1}/User/login-userInfo")
    suspend fun requestGetUserInfo(
        @Header("Authorization") token : String
    ) : Response<UserInfoResponseModel>


    @GET("${v1}/User/login-getPersmissions")
    suspend fun requestUserPermission(
        @Header("Authorization") token : String
    ) : Response<UserPermissionResponseModel>


    @POST("$v1/ManagerAppValidation/managerApp-Validation-Orders-View")
    suspend fun requestGetOrder(
        @Body request: OrderRequestModel,
        @Header("Authorization") token : String
    ): Response<OrderResponseModel>


    @GET("$v1/ManagerAppValidation/managerApp-Validation-OrderDetails-View")
    suspend fun requestOrderDetail(
        @Query("orderId") orderId: Long,
        @Header("Authorization") token : String
    ): Response<DetailOrderResponseModel>

}