package com.hoomanholding.applibrary.model.api

import com.hoomanholding.applibrary.model.data.database.entity.*
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptDetailEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptEntity
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.data.request.*
import com.hoomanholding.applibrary.model.data.response.GeneralResponse
import com.hoomanholding.applibrary.model.data.response.currency.CurrencyModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.model.data.response.reason.DisApprovalReasonModel
import com.hoomanholding.applibrary.model.data.response.report.*
import com.hoomanholding.applibrary.model.data.response.update.AppVersionModel
import com.hoomanholding.applibrary.model.data.response.user.VerifyCodeModel
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


/**
 * Created by m-latifi on 11/26/2022.
 */

interface Api {

    companion object {
        const val api = "/Api"
        const val v1 = "$api/V1"
        const val user = "$v1/User"
        const val baseData = "$v1/BaseData"
        const val saleOrders = "$v1/SaleOrders"
        const val customer = "$v1/Customers"
        const val warehouseReceipt = "$v1/WarehouseReceipt"
        const val report = "$v1/report"
        const val files = "$v1/Files"
        const val customerApp = "/customerApp"
    }

    //---------------------------------------------------------------------------------------------- user
    @POST("$user/login-users")
    suspend fun requestLogin(
        @Body login: LoginRequestModel
    ): Response<GeneralResponse<String?>>


    @GET("$user/login-userInfo")
    suspend fun requestGetUserInfo(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<UserInfoEntity?>>


    @GET("$user/login-getPersmissions")
    suspend fun requestUserPermission(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<String>?>>


    @GET("$user/login-customers-isVerificationCodeCorrect")
    suspend fun requestVerifyCode(
        @Query("verificationCode") verificationCode: String,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<VerifyCodeModel?>>

    @GET("$user/login-ChangePassword")
    suspend fun requestChangePassword(
        @Query("password") password: String,
        @Query("newPassword") newPassword: String,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @GET("$user/login-Resend-VerificationCode")
    suspend fun requestResendVerificationCode(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>
    //---------------------------------------------------------------------------------------------- user


    //---------------------------------------------------------------------------------------------- baseData
    @GET("$baseData/basedata-get-supplier")
    suspend fun requestGetSuppliers(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<SupplierEntity>?>>


    @GET("$baseData/basedata-get-products")
    suspend fun requestGetProducts(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<ProductsEntity>?>>


    @GET("$baseData/basedata-get-brands")
    suspend fun requestGetBrands(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<BrandEntity>?>>

    @GET("$baseData/basedata-get-visitors")
    suspend fun requestGetVisitor(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<VisitorModel>?>>

    @GET("$baseData/basedata-get-disapprovalreasons")
    suspend fun requestDisApprovalReasons(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<DisApprovalReasonModel>?>>

    @GET("$baseData/basedata-get-locations")
    suspend fun requestGetLocations(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<LocationEntity>?>>

    @GET("$baseData/basedata-get-currency")
    suspend fun requestGetCurrency(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CurrencyModel>?>>

    @GET("$baseData/basedata-get-customerproductlist")
    suspend fun requestGetCustomerProducts(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<ProductModel>?>>
    //---------------------------------------------------------------------------------------------- baseData


    //---------------------------------------------------------------------------------------------- saleOrders
    @POST("$saleOrders/managerApp-OrderValidation-Orders-View")
    suspend fun requestGetOrder(
        @Body request: OrderRequestModel,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<OrderModel>?>>


    @GET("$saleOrders/managerApp-OrderValidation-OrderDetails-View")
    suspend fun requestOrderDetail(
        @Query("orderId") orderId: Long,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<OrderModel?>>

    @POST("$saleOrders/managerApp-OrderValidation-Order-ToggleState")
    suspend fun requestOrderToggleState(
        @Body request: OrderToggleStateRequest,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @GET("$saleOrders$customerApp-Orders-View")
    suspend fun requestCustomerGetOrder(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerOrderModel>?>>
    //---------------------------------------------------------------------------------------------- saleOrders


    //---------------------------------------------------------------------------------------------- Customers
    @GET("$customer/basedata-get-customers")
    suspend fun requestGetCustomer(
        @Query("VisitorId") visitorId: Int,
        @Query("strFilter") strFilter: String,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerModel>?>>

    @GET("$customer/financial-get-details")
    suspend fun requestGetCustomerFinancial(
        @Query("CustomerId") customerId: Int,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<CustomerFinancialModel?>>

    @GET("$customer/financial-get-check-details")
    suspend fun requestGetCustomerFinancialDetail(
        @Query("CustomerId") customerId: Int,
        @Query("checktype") checktype: EnumCheckType,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerFinancialDetailModel>?>>
    //---------------------------------------------------------------------------------------------- Customers


    //---------------------------------------------------------------------------------------------- warehouseReceipt
    @GET("$warehouseReceipt/warehouse-receipt-Get")
    suspend fun requestGetReceipts(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<ReceiptEntity>?>>

    @GET("$warehouseReceipt/warehouse-receipt-Get-Details")
    suspend fun requestGetReceiptDetail(
        @Query("ReceiptId") id: Long,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<ReceiptDetailEntity>?>>


    @POST("$warehouseReceipt/warehouse-receipt-add")
    suspend fun requestAddWarehouseReceipt(
        @Body addWarehouseReceipt: AddWarehouseReceipt,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Long?>>

    @GET("$warehouseReceipt/warehouse-receipt-confirm-receipt")
    suspend fun requestConfirmReceipt(
        @Query("ReceiptId") id: Long,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>
    //---------------------------------------------------------------------------------------------- warehouseReceipt


    //---------------------------------------------------------------------------------------------- requestFirstPageReport
    @GET("$report/managerapp-get-firstPageReport")
    suspend fun requestFirstPageReport(
        @Query("currencyTypeId") currencyTypeId: Int,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<HomeReportModel?>>

    @GET("$report/managerapp-report-visitorSalesReport")
    suspend fun requestVisitorSalesReport(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<VisitorSalesReportModel>?>>

    @GET("$report/managerapp-report-customerBalanceReport")
    suspend fun requestCustomerBalance(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerBalanceReportModel>?>>

    @GET("$report/managerapp-report-customerBalanceDetailsReport")
    suspend fun requestCustomerBalanceDetail(
        @Query("CustomerId") customerId: Int,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerBalanceReportDetailModel>?>>

    @GET("$report/managerapp-report-customersBouncedCheckReport")
    suspend fun requestCustomersBouncedCheckReport(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerBounceCheckReportModel>?>>
    //---------------------------------------------------------------------------------------------- requestFirstPageReport


    //---------------------------------------------------------------------------------------------- downloadApkFile
    @GET("$files/files-get-appVersion")
    suspend fun requestGetAppVersion(
        @Query("app") app: String
    ): Response<GeneralResponse<AppVersionModel?>>

    @Streaming
    @GET("$files/files-get-apk")
    suspend fun downloadApkFile(
        @Query("SystemType") SystemType: String,
        @Query("EntityType") EntityType: EnumEntityType,
        @Query("FileName") FileName: String
    ): Response<ResponseBody>


    @Multipart
    @POST("$files/files-add-file")
    suspend fun uploadProfileImage(
        @Part("entityType") entityType: RequestBody,
        @Part File: MultipartBody.Part,
        @HeaderMap Authorization: Map<String, String>
    ): Response<GeneralResponse<Boolean?>>


    //---------------------------------------------------------------------------------------------- downloadApkFile

}