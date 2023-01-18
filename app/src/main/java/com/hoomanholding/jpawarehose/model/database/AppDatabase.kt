package com.hoomanholding.jpawarehose.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hoomanholding.jpawarehose.model.database.dao.ProductDao
import com.hoomanholding.jpawarehose.model.database.dao.RoleDao
import com.hoomanholding.jpawarehose.model.database.dao.SupplierDao
import com.hoomanholding.jpawarehose.model.database.dao.UserInfoDao
import com.hoomanholding.jpawarehose.model.database.entity.ProductsEntity
import com.hoomanholding.jpawarehose.model.database.entity.RoleEntity
import com.hoomanholding.jpawarehose.model.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.model.database.entity.UserInfoEntity


@Database(entities = [
    UserInfoEntity::class,
    RoleEntity::class,
    SupplierEntity::class,
    ProductsEntity::class],
    version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userInfoDao() : UserInfoDao
    abstract fun roleDao() : RoleDao
    abstract fun supplierDao() : SupplierDao
    abstract fun productDao() : ProductDao
}