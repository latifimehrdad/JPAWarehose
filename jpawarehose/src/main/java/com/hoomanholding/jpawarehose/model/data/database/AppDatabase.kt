package com.hoomanholding.jpawarehose.model.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hoomanholding.applibrary.model.data.database.Converters
import com.hoomanholding.applibrary.model.data.database.dao.*
import com.hoomanholding.applibrary.model.data.database.dao.receipt.arrange.ReceiptDao
import com.hoomanholding.applibrary.model.data.database.dao.receipt.arrange.ReceiptDetailDao
import com.hoomanholding.applibrary.model.data.database.dao.receipt.history.HistorySaveReceiptAmountDao
import com.hoomanholding.applibrary.model.data.database.dao.receipt.history.HistorySaveReceiptDao
import com.hoomanholding.applibrary.model.data.database.dao.receipt.save.SaveReceiptAmountDao
import com.hoomanholding.applibrary.model.data.database.dao.receipt.save.SaveReceiptDao
import com.hoomanholding.applibrary.model.data.database.entity.*
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptDetailEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.arrange.ReceiptEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.history.HistorySaveReceiptAmountEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.history.HistorySaveReceiptEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.save.SaveReceiptAmountEntity
import com.hoomanholding.applibrary.model.data.database.entity.receipt.save.SaveReceiptEntity


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
        LocationAmountEntity::class,
        HistorySaveReceiptEntity::class,
        HistorySaveReceiptAmountEntity::class],
    version = 1
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
    abstract fun locationAmountDao(): LocationAmountDao
    abstract fun historySaveReceiptDao(): HistorySaveReceiptDao
    abstract fun historySaveReceiptAmountDao(): HistorySaveReceiptAmountDao
}