package com.hoomanholding.jpamanager.model.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {


    //---------------------------------------------------------------------------------------------- providerUserInfoDao
    @Provides
    @Singleton
    fun providerUserInfoDao(appDatabase: AppDatabase) = appDatabase.userInfoDao()
    //---------------------------------------------------------------------------------------------- providerUserInfoDao


    //---------------------------------------------------------------------------------------------- providerRoleDao
    @Provides
    @Singleton
    fun providerRoleDao(appDatabase: AppDatabase) = appDatabase.roleDao()
    //---------------------------------------------------------------------------------------------- providerRoleDao



    //---------------------------------------------------------------------------------------------- provideAppDatabase
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "jpa"
    ).allowMainThreadQueries().build()
    //---------------------------------------------------------------------------------------------- provideAppDatabase

}