package com.zarholding.jpacustomer.model.data.database

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


    //---------------------------------------------------------------------------------------------- provideAppDatabase
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {

        /*        val migration1to2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE IF NOT EXISTS 'UserRule' ('id' INTEGER NOT NULL, 'userLvl' TEXT NOT NULL, 'canSell' INTEGER NOT NULL, 'canBuyCrypto' INTEGER NOT NULL, 'canBuyEc' INTEGER NOT NULL, 'isNeededTwoStep' INTEGER NOT NULL, 'dailyPurchaseLimit' INTEGER NOT NULL, PRIMARY KEY('id'))")
                    }
                }*/

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

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "jpa"
        ).addMigrations(migration1to2).allowMainThreadQueries().build()
    }
    //---------------------------------------------------------------------------------------------- provideAppDatabase

}