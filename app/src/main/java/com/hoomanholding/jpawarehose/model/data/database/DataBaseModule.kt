package com.hoomanholding.jpawarehose.model.data.database

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
    fun providerSaveReceiptDao(appDatabase: AppDatabase) = appDatabase.saveReceiptDao()
    //---------------------------------------------------------------------------------------------- providerSaveReceiptDap



    //---------------------------------------------------------------------------------------------- providerSaveReceiptAmountDao
    @Provides
    @Singleton
    fun providerSaveReceiptAmountDao(appDatabase: AppDatabase) = appDatabase.saveReceiptAmountDao()
    //---------------------------------------------------------------------------------------------- providerSaveReceiptAmountDao


    //---------------------------------------------------------------------------------------------- providerLocationAmountDao
    @Provides
    @Singleton
    fun providerLocationAmountDao(appDatabase: AppDatabase) = appDatabase.locationAmountDao()
    //---------------------------------------------------------------------------------------------- providerLocationAmountDao



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