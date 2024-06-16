package com.hoomanholding.jpawarehose.model.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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



    //---------------------------------------------------------------------------------------------- provideHistorySaveReceiptDao
    @Provides
    @Singleton
    fun provideHistorySaveReceiptDao(appDatabase: AppDatabase) = appDatabase.historySaveReceiptDao()
    //---------------------------------------------------------------------------------------------- provideHistorySaveReceiptDao



    //---------------------------------------------------------------------------------------------- providerHistorySaveReceiptAmountDao
    @Provides
    @Singleton
    fun providerHistorySaveReceiptAmountDao(appDatabase: AppDatabase) =
        appDatabase.historySaveReceiptAmountDao()
    //---------------------------------------------------------------------------------------------- providerHistorySaveReceiptAmountDao



    //---------------------------------------------------------------------------------------------- provideAppDatabase
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {

        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN mobileNumber TEXT")
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN storeName TEXT")
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN address TEXT")
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN visitorId INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN visitorName TEXT")
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN visitorMobile TEXT")
            }
        }

        val migration2to3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN userTopicstr TEXT")
            }
        }

        val migration3to4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN x REAL DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN y REAL DEFAULT 0 NOT NULL")
            }
        }

        val migration4to5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE UserInfo ADD COLUMN isSubset INTEGER DEFAULT 0 NOT NULL")
            }
        }

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "jpa"
        ).addMigrations(migration1to2,migration2to3, migration3to4, migration4to5).allowMainThreadQueries().build()
    }
    //---------------------------------------------------------------------------------------------- provideAppDatabase
}