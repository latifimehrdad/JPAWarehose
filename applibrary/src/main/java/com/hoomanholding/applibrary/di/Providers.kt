package com.hoomanholding.applibrary.di

import android.content.Context
import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.tools.CompanionValues.Companion.URL
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
//        var url = "http://api.hoomanholding.com:9090" // Jpa
        var url = "http://10.252.37.37:7575" //Zar
    }

    //---------------------------------------------------------------------------------------------- provideUrl
    @Provides
    @Singleton
    fun provideUrl(@ApplicationContext appContext: Context): String {
        val share = appContext.getSharedPreferences("secret_shared_prefs", Context.MODE_PRIVATE)
        val ip = share.getString(URL, null)
        ip?.let { url = ip }
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