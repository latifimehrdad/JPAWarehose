package com.hoomanholding.jpawarehose.model.database

import android.content.Context
import androidx.room.Room
import com.hoomanholding.jpawarehose.model.database.dao.*
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


    //---------------------------------------------------------------------------------------------- providerSupplierDao
    @Provides
    @Singleton
    fun providerSupplierDao(appDatabase: AppDatabase) = appDatabase.supplierDao()
    //---------------------------------------------------------------------------------------------- providerSupplierDao


    //---------------------------------------------------------------------------------------------- providerProductDao
    @Provides
    @Singleton
    fun providerProductDao(appDatabase: AppDatabase) = appDatabase.productDao()
    //---------------------------------------------------------------------------------------------- providerProductDao



    //---------------------------------------------------------------------------------------------- providerBrandDao
    @Provides
    @Singleton
    fun providerBrandDao(appDatabase: AppDatabase) = appDatabase.brandDao()
    //---------------------------------------------------------------------------------------------- providerBrandDao



    //---------------------------------------------------------------------------------------------- providerReceiptDao
    @Provides
    @Singleton
    fun providerReceiptDao(appDatabase: AppDatabase) = appDatabase.receiptDao()
    //---------------------------------------------------------------------------------------------- providerReceiptDao



    //---------------------------------------------------------------------------------------------- providerReceiptDetailDao
    @Provides
    @Singleton
    fun providerReceiptDetailDao(appDatabase: AppDatabase) = appDatabase.receiptDetailDao()
    //---------------------------------------------------------------------------------------------- providerReceiptDetailDao


    //---------------------------------------------------------------------------------------------- providerLocationDao
    @Provides
    @Singleton
    fun providerLocationDao(appDatabase: AppDatabase) = appDatabase.locationDao()
    //---------------------------------------------------------------------------------------------- providerLocationDao



    //---------------------------------------------------------------------------------------------- providerSaveReceiptDap
    @Provides
    @Singleton
    fun providerSaveReceiptDap(appDatabase: AppDatabase) = appDatabase.saveReceiptDao()
    //---------------------------------------------------------------------------------------------- providerSaveReceiptDap



    //---------------------------------------------------------------------------------------------- providerProductSaveReceiptDap
    @Provides
    @Singleton
    fun providerProductSaveReceiptDap(appDatabase: AppDatabase) = appDatabase.productSaveReceiptDao()
    //---------------------------------------------------------------------------------------------- providerProductSaveReceiptDap



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