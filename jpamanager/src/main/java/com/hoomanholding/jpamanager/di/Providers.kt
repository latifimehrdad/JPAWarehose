package com.hoomanholding.jpamanager.di

import android.content.Context
import com.hoomanholding.jpamanager.model.api.Api
import com.hoomanholding.jpamanager.tools.CompanionValues.Companion.URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by m-latifi on 11/8/2022.
 */

@Module
@InstallIn(SingletonComponent::class)
class Providers {

    companion object {
        //        const val url = "http://5.160.125.98:5081"
//        const val url = "http://192.168.50.153:8081"
        const val url = "http://10.252.37.46:7575"
//        const val url = "http://192.168.70.176:8595"
    }

    //---------------------------------------------------------------------------------------------- provideUrl
    @Provides
    @Singleton
    fun provideUrl(@ApplicationContext appContext: Context): String {
        val share = appContext.getSharedPreferences("secret_shared_prefs", Context.MODE_PRIVATE)
        val ip = share.getString(URL, null)
        return ip ?: run { url }
    }
    //---------------------------------------------------------------------------------------------- provideUrl


    //---------------------------------------------------------------------------------------------- provideApi
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api =
        retrofit.create(Api::class.java)
    //---------------------------------------------------------------------------------------------- provideApi


}