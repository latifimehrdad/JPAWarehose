package com.hoomanholding.applibrary.model.api

import com.hoomanholding.applibrary.model.data.database.entity.*
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptDetailEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptEntity
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.data.enums.EnumState
import com.hoomanholding.applibrary.model.data.request.*
import com.hoomanholding.applibrary.model.data.response.GeneralResponse
import com.hoomanholding.applibrary.model.data.response.about.AboutModel
import com.hoomanholding.applibrary.model.data.response.basedata.ComboModel
import com.hoomanholding.applibrary.model.data.response.basket.BasketModel
import com.hoomanholding.applibrary.model.data.response.category.CategoryModel
import com.hoomanholding.applibrary.model.data.response.check.RecordCheckNotifyModel
import com.hoomanholding.applibrary.model.data.response.critic.CriticDetailModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerStateModel
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderDetailModel
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.model.data.response.report.*
import com.hoomanholding.applibrary.model.data.response.critic.CriticModel
import com.hoomanholding.applibrary.model.data.response.basket.subuser.SubUserOrderModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomersModel
import com.hoomanholding.applibrary.model.data.response.update.AppVersionModel
import com.hoomanholding.applibrary.model.data.response.user.UserModel
import com.hoomanholding.applibrary.model.data.response.user.VerifyCodeModel
import com.hoomanholding.applibrary.model.data.response.video.VideoCategoryModel
import com.hoomanholding.applibrary.model.data.response.video.VideoModel
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
        const val customers = "$v1/Customers"
        const val warehouseReceipt = "$v1/WarehouseReceipt"
        const val report = "$v1/report"
        const val files = "$v1/Files"
        const val customerApp = "/customerApp"
        const val basket = "$v1/Basket"
        const val video = "$v1/Video"
        const val about = "$v1/AboutUs"
        const val critics = "$v1/Critics"
    }

    //---------------------------------------------------------------------------------------------- user
    @POST("$user/login-users")
    suspend fun requestLogin(
        @Body login: LoginRequestModel
    ): Response<GeneralResponse<String?>>


    @GET("$user/login-Preapering-ForgetPassword")
    suspend fun requestForgetPassword(
        @Query("systemTypesEnum") systemTypesEnum: String,
        @Query("userName") userName: String,
        @Query("androidId") androidId: String
    ): Response<GeneralResponse<String?>>


    @GET("$user/login-userInfo")
    suspend fun requestGetUserInfo(
        @Query("UserToken") fireBaseToken: String?,
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

    @GET("$user/login-ChangeForgotenPassword")
    suspend fun requestForgetChangePassword(
        @Query("newPassword") newPassword: String,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @GET("$user/login-Resend-VerificationCode")
    suspend fun requestResendVerificationCode(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @GET("$user/getNotRecordCheck")
    suspend fun requestGetNotRecordCheck(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<RecordCheckNotifyModel?>>
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

    @GET("$baseData/basedata-get-baseDataCombo")
    suspend fun requestBaseDataComb(
        @Query("type") type: Int,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<ComboModel>?>>

    @GET("$baseData/basedata-get-locations")
    suspend fun requestGetLocations(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<LocationEntity>?>>

    @GET("$baseData/basedata-get-customerproductlist")
    suspend fun requestGetCustomerProducts(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<ProductModel>?>>

    @GET("$baseData/basedata-SaveError")
    suspend fun requestSaveError(
        @Query("ErrorText") errorText: String,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @GET("$baseData/basedata-GetProductCategory")
    suspend fun requestGetProductCategory(
            @Query("lvl") lvl: Int,
            @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CategoryModel>?>>

    @GET("$baseData/basedata-get-customerReturnProductList")
    suspend fun requestGetCustomerReturnProducts(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<ProductModel>?>>


    @GET("$baseData/basedata-get-Customrs")
    suspend fun requestGetCustomers(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomersModel>?>>
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

    @GET("$saleOrders$customerApp-Order-Details-View")
    suspend fun requestGetCustomerOrderDetail(
        @Query("orderId") orderId: Long,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerOrderDetailModel>?>>
    //---------------------------------------------------------------------------------------------- saleOrders


    //---------------------------------------------------------------------------------------------- Customers
    @GET("$customers/basedata-get-customers")
    suspend fun requestGetCustomer(
        @Query("VisitorId") visitorId: Int,
        @Query("strFilter") strFilter: String,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerModel>?>>

    @GET("$customers/financial-get-details")
    suspend fun requestGetCustomerFinancial(
        @Query("CustomerId") customerId: Int,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<CustomerFinancialModel?>>

    @GET("$customers/financial-get-check-details")
    suspend fun requestGetCustomerFinancialDetail(
        @Query("CustomerId") customerId: Int,
        @Query("checktype") checktype: EnumCheckType,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerFinancialDetailModel>?>>

    @GET("$customers/get-customer-state")
    suspend fun requestGetCustomerState(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<CustomerStateModel?>>

    @GET("$customers/Edit-customer-Location")
    suspend fun requestEditCustomerLocation(
        @Query("x") lat: Double,
        @Query("y") long: Double,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>


    @GET("$customers/basedata-get-all-customers")
    suspend fun requestGetAllCustomers(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<UserModel>?>>

    @POST("$customers/Add-customer-Licensing")
    suspend fun requestAddCustomerLicensing(
        @Body request: List<Long>,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>
    //---------------------------------------------------------------------------------------------- Customers


    //---------------------------------------------------------------------------------------------- basket
    @GET("$basket/basket-details")
    suspend fun requestGetDetailBasket(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<BasketModel?>>

    @GET("$basket/returnbasket-details")
    suspend fun requestGetDetailReturnBasket(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<BasketModel?>>

    @POST("$basket/basket-add")
    suspend fun requestAddToBasket(
        @Body request: List<AddToBasket>,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @POST("$basket/returnbasket-add")
    suspend fun requestAddToReturnBasket(
        @Body request: List<AddToBasket>,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @GET("$basket/basket-details_count")
    suspend fun requestGetBasketCount(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Int?>>

    @GET("$basket/basket-delete")
    suspend fun requestDeleteBasket(
        @Query("productId") productId: Int,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @GET("$basket/basket-deleteAll")
    suspend fun requestDeleteAllBasket(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>


    @GET("$basket/returnbasket-delete")
    suspend fun requestDeleteReturnBasket(
        @Query("productId") productId: Int,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @GET("$basket/returnbasket-deleteAll")
    suspend fun requestDeleteReturnAllBasket(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>

    @GET("$basket/basket-submit")
    suspend fun requestSubmitBasket(
        @Query("description") description: String,
        @Query("exhibition") exhibition: Boolean,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Long?>>

    @GET("$basket/returnbasket-submit")
    suspend fun requestSubmitReturnBasket(
        @Query("description") description: String,
        @Query("exhibition") exhibition: Boolean,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Long?>>


    @GET("$basket/SubsetBasket")
    suspend fun requestSubsetBasket(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<SubUserOrderModel>?>>

    @GET("$basket/SubsetCustomerBasket-submit")
    suspend fun requestSubmitSubUserBasket(
        @Query("Id") id: String,
        @Query("state") state: EnumState,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Long?>>
    //---------------------------------------------------------------------------------------------- basket




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
        @Query("CustomerId") customerId: Long,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerBalanceReportDetailModel>?>>

    @GET("$report/managerapp-report-customersBouncedCheckReport")
    suspend fun requestCustomersBouncedCheckReport(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerBounceCheckReportModel>?>>

    @GET("$report/customers-report-customersBillingReport")
    suspend fun requestCustomerBillingReport(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("customerId") customerId: Long?,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<BillingAndReturnReportModel>?>>

    @GET("$report/customers-report-customersReturnReport")
    suspend fun requestCustomerReturnReport(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("customerId") customerId: Long?,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<BillingAndReturnReportModel>?>>


    @GET("$report/customers-report-customerOrderReport")
    suspend fun requestCustomerOrderReport(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<ReportCustomerOrderModel>?>>


    @GET("$report/customers-report-customerHeaderBillingsPDF")
    suspend fun requestCustomerHeaderBillingsPDF(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("type") type: String,
        @Query("customerId") customerId: Long?,
        @Header("Authorization") token: String
    ): Response<ResponseBody>


    @GET("$report/customers-report-customersBillingPDF")
    suspend fun requestCustomersBillingPDF(
        @Query("billingId") billingId: Long,
        @Query("type") type: String,
        @Query("customerId") customerId: Long?,
        @Header("Authorization") token: String
    ): Response<ResponseBody>


    @GET("$report/customers-report-customerBalancePDF")
    suspend fun requestCustomerBalancePDF(
        @Header("Authorization") token: String
    ): Response<ResponseBody>


    @GET("$report/customers-report-customerReturnBasketReport")
    suspend fun requestCustomerReturnBasketReport(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CustomerReturnBasketReportModel>?>>


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



    //---------------------------------------------------------------------------------------------- critics
    @GET("$critics/CriticsAndOffers_Add")
    suspend fun addNewCritic(
        @Query("Tittle") tittle: String,
        @Query("Message") message: String,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Int?>>

    @GET("$critics/CriticsAndOffers_GetList")
    suspend fun getCriticsList(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CriticModel>?>>

    @GET("$critics/CriticsAndOffers_GetDetails")
    suspend fun getCriticDetailList(
        @Query("CriticsAndOffersId") criticsAndOffersId: Long,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<CriticDetailModel>?>>

    @GET("$critics/CriticsAndOffers_reply")
    suspend fun sendReplyCritic(
        @Query("Message") message: String,
        @Query("CriticsAndOffersId") id: Long,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<Boolean?>>
    //---------------------------------------------------------------------------------------------- critics


    //---------------------------------------------------------------------------------------------- video
    @GET("$video/get-videoGroups")
    suspend fun getVideoCategory(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<VideoCategoryModel>?>>

    @GET("$video/get-videos")
    suspend fun getVideo(
        @Query("videoGroupId") videoGroupId: Int,
        @Header("Authorization") token: String
    ): Response<GeneralResponse<List<VideoModel>?>>
    //---------------------------------------------------------------------------------------------- video


    //---------------------------------------------------------------------------------------------- about
    @GET("$about/get-aboutUs")
    suspend fun getAbout(
        @Header("Authorization") token: String
    ): Response<GeneralResponse<AboutModel?>>
    //---------------------------------------------------------------------------------------------- about







}