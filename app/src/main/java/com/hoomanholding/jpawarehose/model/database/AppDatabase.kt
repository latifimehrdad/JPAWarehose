package com.hoomanholding.jpawarehose.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hoomanholding.jpawarehose.model.database.dao.*
import com.hoomanholding.jpawarehose.model.database.entity.*


@Database(
    entities = [
        UserInfoEntity::class,
        RoleEntity::class,
        SupplierEntity::class,
        ProductsEntity::class,
        BrandEntity::class,
        ReceiptEntity::class,
        ReceiptDetailEntity::class,
        LocationEntity::class,
        SaveReceiptEntity::class,
        SaveReceiptAmountEntity::class,
        LocationAmountEntity::class],
    version = 1, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao
    abstract fun roleDao(): RoleDao
    abstract fun supplierDao(): SupplierDao
    abstract fun productDao(): ProductDao
    abstract fun brandDao(): BrandDao
    abstract fun receiptDao(): ReceiptDao
    abstract fun receiptDetailDao(): ReceiptDetailDao
    abstract fun locationDao(): LocationDao
    abstract fun saveReceiptDao(): SaveReceiptDao
    abstract fun saveReceiptAmountDao(): SaveReceiptAmountDao
    abstract fun locationAmountDao() : LocationAmountDao
}