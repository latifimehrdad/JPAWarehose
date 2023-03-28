package com.hoomanholding.jpamanager.model.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hoomanholding.jpamanager.model.data.database.dao.RoleDao
import com.hoomanholding.jpamanager.model.data.database.dao.UserInfoDao
import com.hoomanholding.jpamanager.model.data.database.entity.RoleEntity
import com.hoomanholding.jpamanager.model.data.database.entity.UserInfoEntity


@Database(
    entities = [
        UserInfoEntity::class,
        RoleEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao
    abstract fun roleDao(): RoleDao
}