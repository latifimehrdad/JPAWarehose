package com.hoomanholding.jpawarehose.model.api

import com.hoomanholding.jpawarehose.model.data.request.LoginRequestModel
import com.hoomanholding.jpawarehose.model.data.response.brand.BrandResponseModel
import com.hoomanholding.jpawarehose.model.data.response.product.ProductResponseModel
import com.hoomanholding.jpawarehose.model.data.response.receipt.ReceiptResponseModel
import com.hoomanholding.jpawarehose.model.data.response.supplier.SupplierResponseModel
import com.hoomanholding.jpawarehose.model.data.response.user.UserInfoResponseModel
import com.hoomanholding.jpawarehose.model.data.response.user.LoginResponseModel
import com.hoomanholding.jpawarehose.model.data.response.user.UserPermissionResponseModel
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


    @GET("${v1}/BaseData/basedata-get-supplier")
    suspend fun requestGetSuppliers(
        @Header("Authorization") token : String
    ) : Response<SupplierResponseModel>


    @GET("${v1}/BaseData/basedata-get-products")
    suspend fun requestGetProducts(
        @Header("Authorization") token : String
    ) : Response<ProductResponseModel>


    @GET("${v1}/BaseData/basedata-get-brands")
    suspend fun requestGetBrands(
        @Header("Authorization") token : String
    ) : Response<BrandResponseModel>


    @GET("${v1}/WarehouseReceipt/warehouse-receipt-Get")
    suspend fun requestGetReceipts(
        @Header("Authorization") token : String
    ) : Response<ReceiptResponseModel>
}