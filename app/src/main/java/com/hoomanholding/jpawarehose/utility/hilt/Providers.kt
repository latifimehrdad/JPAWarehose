package com.hoomanholding.jpawarehose.utility.hilt

import com.hoomanholding.jpawarehose.model.api.ApiSuperApp
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
        const val url = "http://192.168.50.153:9090"
    }

    //---------------------------------------------------------------------------------------------- provideBPMSUrl
    @Provides
    @Singleton
    fun provideBPMSUrl() = "http://192.168.50.153:9090"
    //---------------------------------------------------------------------------------------------- provideBPMSUrl


    //---------------------------------------------------------------------------------------------- provideApiBPMS
    @Provides
    @Singleton
    fun provideApiBPMS(retrofit: Retrofit) : ApiSuperApp =
        retrofit.create(ApiSuperApp::class.java)
    //---------------------------------------------------------------------------------------------- provideApiBPMS


}