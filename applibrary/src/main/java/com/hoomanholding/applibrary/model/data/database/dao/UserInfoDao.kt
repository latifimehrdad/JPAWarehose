package com.hoomanholding.applibrary.model.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity


@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(userInfoEntity: UserInfoEntity)

    @Query("SELECT * FROM UserInfo LIMIT 1")
    fun getUserInfo(): UserInfoEntity?

    @Query("DELETE FROM userinfo")
    fun deleteAll()


    @Query("UPDATE UserInfo SET x = :x, y = :y")
    fun updateXY(x: Double, y: Double)

}