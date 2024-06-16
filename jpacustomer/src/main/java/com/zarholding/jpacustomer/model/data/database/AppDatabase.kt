package com.zarholding.jpacustomer.model.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hoomanholding.applibrary.model.data.database.Converters
import com.hoomanholding.applibrary.model.data.database.dao.RoleDao
import com.hoomanholding.applibrary.model.data.database.dao.UserInfoDao
import com.hoomanholding.applibrary.model.data.database.entity.RoleEntity
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity


@Database(
    entities = [
        UserInfoEntity::class,
        RoleEntity::class],
    version = 5
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao
    abstract fun roleDao(): RoleDao
}