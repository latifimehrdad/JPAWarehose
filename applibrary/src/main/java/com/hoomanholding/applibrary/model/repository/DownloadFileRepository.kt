package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.di.Providers
import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.zar.core.tools.api.apiCall
import com.zar.core.tools.hilt.ProgressResponseBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * create by m-latifi on 4/25/2023
 */

class DownloadFileRepository @Inject constructor() {

    @Inject
    lateinit var tokenRepository: TokenRepository

    //---------------------------------------------------------------------------------------------- downloadApkFile
    suspend fun downloadApkFile(systemType: String, fileName: String) =
        retrofit().create(Api::class.java).downloadApkFile(systemType, EnumEntityType.APK, fileName)
    //---------------------------------------------------------------------------------------------- downloadApkFile



    //---------------------------------------------------------------------------------------------- downloadCustomerBalancePDF
    suspend fun downloadCustomerBalancePDF() =
        retrofit().create(Api::class.java).requestCustomerBalancePDF(tokenRepository.getBearerToken())
    //---------------------------------------------------------------------------------------------- downloadCustomerBalancePDF



    //---------------------------------------------------------------------------------------------- downloadCustomersBillingPDF
    suspend fun downloadCustomersBillingPDF(billingId: Long, type: String, customerId: Long?) =
        retrofit().create(Api::class.java)
            .requestCustomersBillingPDF(billingId, type, customerId, tokenRepository.getBearerToken())

    //---------------------------------------------------------------------------------------------- downloadCustomersBillingPDF



    //---------------------------------------------------------------------------------------------- requestCustomerHeaderBillingsPDF
    suspend fun requestCustomerHeaderBillingsPDF(fromDate: String, toDate: String, type: String, customerId: Long?) =
            retrofit().create(Api::class.java).requestCustomerHeaderBillingsPDF(
                fromDate, toDate, type, customerId, tokenRepository.getBearerToken()
            )
    //---------------------------------------------------------------------------------------------- requestCustomerHeaderBillingsPDF



    //---------------------------------------------------------------------------------------------- retrofit
    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Providers.url)
        .client(httpClient())
        .build()
    //---------------------------------------------------------------------------------------------- retrofit


    //---------------------------------------------------------------------------------------------- httpClient
    private fun httpClient() = OkHttpClient()
        .newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(interceptor())
        .build()
    //---------------------------------------------------------------------------------------------- httpClient


    //---------------------------------------------------------------------------------------------- interceptor
    private fun interceptor() = Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        originalResponse.newBuilder()
            .body(ProgressResponseBody(originalResponse.body!!) { _, _, _ -> })
            .build()
    }
    //---------------------------------------------------------------------------------------------- interceptor

}