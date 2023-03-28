package com.hoomanholding.applibrary.model.api

import com.hoomanholding.applibrary.model.data.request.AddWarehouseReceipt
import com.hoomanholding.applibrary.model.data.request.LoginRequestModel
import com.hoomanholding.applibrary.model.data.request.OrderRequestModel
import com.hoomanholding.applibrary.model.data.response.AddWarehouseReceiptResponseModel
import com.hoomanholding.applibrary.model.data.response.brand.BrandResponseModel
import com.hoomanholding.applibrary.model.data.response.location.LocationResponseModel
import com.hoomanholding.applibrary.model.data.response.order.DetailOrderResponseModel
import com.hoomanholding.applibrary.model.data.response.order.OrderResponseModel
import com.hoomanholding.applibrary.model.data.response.product.ProductResponseModel
import com.hoomanholding.applibrary.model.data.response.receipt.ConfirmWarehouseReceiptResponseModel
import com.hoomanholding.applibrary.model.data.response.receipt.ReceiptDetailResponseModel
import com.hoomanholding.applibrary.model.data.response.receipt.ReceiptResponseModel
import com.hoomanholding.applibrary.model.data.response.supplier.SupplierResponseModel
import com.hoomanholding.applibrary.model.data.response.user.LoginResponseModel
import com.hoomanholding.applibrary.model.data.response.user.UserInfoResponseModel
import com.hoomanholding.applibrary.model.data.response.user.UserPermissionResponseModel
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


    @GET("$v1/User/login-userInfo")
    suspend fun requestGetUserInfo(
        @Header("Authorization") token : String
    ) : Response<UserInfoResponseModel>


    @GET("$v1/User/login-getPersmissions")
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

    @GET("${v1}/WarehouseReceipt/warehouse-receipt-Get-Details")
    suspend fun requestGetReceiptDetail(
        @Query("ReceiptId") id : Long,
        @Header("Authorization") token : String
    ) : Response<ReceiptDetailResponseModel>


    @GET("${v1}/BaseData/basedata-get-locations")
    suspend fun requestGetLocations(
        @Header("Authorization") token : String
    ) : Response<LocationResponseModel>


    @POST("${v1}/WarehouseReceipt/warehouse-receipt-add")
    suspend fun requestAddWarehouseReceipt(
        @Body addWarehouseReceipt: AddWarehouseReceipt,
        @Header("Authorization") token : String
    ) : Response<AddWarehouseReceiptResponseModel>

    @GET("${v1}/WarehouseReceipt/warehouse-receipt-confirm-receipt")
    suspend fun requestConfirmReceipt(
        @Query("ReceiptId") id : Long,
        @Header("Authorization") token : String
    ) : Response<ConfirmWarehouseReceiptResponseModel>

}