package com.hoomanholding.jpawarehose.utility.hilt

import com.hoomanholding.jpawarehose.model.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        const val url = "http://10.252.30.150:7575"
    }

    //---------------------------------------------------------------------------------------------- provideUrl
    @Provides
    @Singleton
    fun provideUrl() = "http://10.252.30.150:7575"
    //---------------------------------------------------------------------------------------------- provideUrl


    //---------------------------------------------------------------------------------------------- provideApi
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit) : Api =
        retrofit.create(Api::class.java)
    //---------------------------------------------------------------------------------------------- provideApi


}