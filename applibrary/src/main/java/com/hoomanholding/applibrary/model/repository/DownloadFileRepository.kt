package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.di.Providers
import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
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

    //---------------------------------------------------------------------------------------------- downloadApkFile
    suspend fun downloadApkFile(systemType: String, fileName: String) =
        retrofit().create(Api::class.java).downloadApkFile(systemType, EnumEntityType.APK, fileName)
    //---------------------------------------------------------------------------------------------- downloadApkFile


    //---------------------------------------------------------------------------------------------- retrofit
    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Providers.url)
        .client(httpClient())
        .build()
    //---------------------------------------------------------------------------------------------- retrofit


    //---------------------------------------------------------------------------------------------- httpClient
    private fun httpClient() = OkHttpClient()
        .newBuilder()
        .connectTimeout(20, TimeUnit.SECONDS)
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