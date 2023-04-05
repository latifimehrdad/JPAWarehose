package com.hoomanholding.applibrary.model.api

import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.request.AddWarehouseReceipt
import com.hoomanholding.applibrary.model.data.request.LoginRequestModel
import com.hoomanholding.applibrary.model.data.request.OrderRequestModel
import com.hoomanholding.applibrary.model.data.response.AddWarehouseReceiptResponseModel
import com.hoomanholding.applibrary.model.data.response.brand.BrandResponseModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailResponseModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialResponseModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerResponseModel
import com.hoomanholding.applibrary.model.data.response.location.LocationResponseModel
import com.hoomanholding.applibrary.model.data.response.order.DetailOrderResponseModel
import com.hoomanholding.applibrary.model.data.response.order.OrderResponseModel
import com.hoomanholding.applibrary.model.data.response.product.ProductResponseModel
import com.hoomanholding.applibrary.model.data.response.reason.DisApprovalReasonResponseModel
import com.hoomanholding.applibrary.model.data.response.receipt.ConfirmWarehouseReceiptResponseModel
import com.hoomanholding.applibrary.model.data.response.receipt.ReceiptDetailResponseModel
import com.hoomanholding.applibrary.model.data.response.receipt.ReceiptResponseModel
import com.hoomanholding.applibrary.model.data.response.supplier.SupplierResponseModel
import com.hoomanholding.applibrary.model.data.response.user.LoginResponseModel
import com.hoomanholding.applibrary.model.data.response.user.UserInfoResponseModel
import com.hoomanholding.applibrary.model.data.response.user.UserPermissionResponseModel
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorResponseModel
import retrofit2.Response
import retrofit2.http.*


/**
 * Created by m-latifi on 11/26/2022.
 */

interface Api {

    companion object {
        const val api = "/Api"
        const val v1 = "$api/V1"
        const val baseData = "$v1/BaseData"
        const val saleOrders = "$v1/SaleOrders"
        const val customer = "$v1/Customers"
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


    //---------------------------------------------------------------------------------------------- baseData
    @GET("$baseData/basedata-get-supplier")
    suspend fun requestGetSuppliers(
        @Header("Authorization") token : String
    ) : Response<SupplierResponseModel>


    @GET("$baseData/basedata-get-products")
    suspend fun requestGetProducts(
        @Header("Authorization") token : String
    ) : Response<ProductResponseModel>


    @GET("$baseData/basedata-get-brands")
    suspend fun requestGetBrands(
        @Header("Authorization") token : String
    ) : Response<BrandResponseModel>

    @GET("$baseData/basedata-get-visitors")
    suspend fun requestGetVisitor(
        @Header("Authorization") token : String
    ): Response<VisitorResponseModel>

    @GET("$baseData/basedata-get-disapprovalreasons")
    suspend fun requestDisApprovalReasons(
        @Header("Authorization") token : String
    ): Response<DisApprovalReasonResponseModel>
    //---------------------------------------------------------------------------------------------- baseData


    //---------------------------------------------------------------------------------------------- saleOrders
    @POST("$saleOrders/managerApp-OrderValidation-Orders-View")
    suspend fun requestGetOrder(
        @Body request: OrderRequestModel,
        @Header("Authorization") token : String
    ): Response<OrderResponseModel>


    @GET("$saleOrders/managerApp-OrderValidation-OrderDetails-View")
    suspend fun requestOrderDetail(
        @Query("orderId") orderId: Int,
        @Header("Authorization") token : String
    ): Response<DetailOrderResponseModel>
    //---------------------------------------------------------------------------------------------- saleOrders



    //---------------------------------------------------------------------------------------------- Customers
    @GET("$customer/basedata-get-customers")
    suspend fun requestGetCustomer(
        @Query("VisitorId") visitorId: Int,
        @Query("strFilter") strFilter: String,
        @Header("Authorization") token : String
    ): Response<CustomerResponseModel>

    @GET("$customer/financial-get-details")
    suspend fun requestGetCustomerFinancial(
        @Query("CustomerId") customerId: Int,
        @Header("Authorization") token : String
    ): Response<CustomerFinancialResponseModel>

    @GET("$customer/financial-get-check-details")
    suspend fun requestGetCustomerFinancialDetail(
        @Query("CustomerId") customerId: Int,
        @Query("checktype") checktype: EnumCheckType,
        @Header("Authorization") token : String
    ): Response<CustomerFinancialDetailResponseModel>
    //---------------------------------------------------------------------------------------------- Customers



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