package com.hoomanholding.jpawarehose.model.database

import android.content.Context
import androidx.room.Room
import com.hoomanholding.jpawarehose.model.database.dao.ProductDao
import com.hoomanholding.jpawarehose.model.database.dao.RoleDao
import com.hoomanholding.jpawarehose.model.database.dao.SupplierDao
import com.hoomanholding.jpawarehose.model.database.dao.UserInfoDao
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
    fun providerUserInfoDao(appDatabase: AppDatabase) : UserInfoDao {
        return appDatabase.userInfoDao()
    }
    //---------------------------------------------------------------------------------------------- providerUserInfoDao


    //---------------------------------------------------------------------------------------------- providerRoleDao
    @Provides
    @Singleton
    fun providerRoleDao(appDatabase: AppDatabase) : RoleDao {
        return appDatabase.roleDao()
    }
    //---------------------------------------------------------------------------------------------- providerRoleDao


    //---------------------------------------------------------------------------------------------- providerSupplierDao
    @Provides
    @Singleton
    fun providerSupplierDao(appDatabase: AppDatabase) : SupplierDao {
        return appDatabase.supplierDao()
    }
    //---------------------------------------------------------------------------------------------- providerSupplierDao


    //---------------------------------------------------------------------------------------------- providerProductDao
    @Provides
    @Singleton
    fun providerProductDao(appDatabase: AppDatabase) : ProductDao {
        return appDatabase.productDao()
    }
    //---------------------------------------------------------------------------------------------- providerProductDao



    //---------------------------------------------------------------------------------------------- provideAppDatabase
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "jpa"
        ).allowMainThreadQueries().build()
    }
    //---------------------------------------------------------------------------------------------- provideAppDatabase

}