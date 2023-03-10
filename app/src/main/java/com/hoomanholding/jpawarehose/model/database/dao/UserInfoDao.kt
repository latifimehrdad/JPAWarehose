package com.hoomanholding.jpawarehose.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoomanholding.jpawarehose.model.database.entity.UserInfoEntity

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(userInfoEntity: UserInfoEntity)

    @Query("SELECT * FROM UserInfo LIMIT 1")
    fun getUserInfo(): UserInfoEntity?

    @Query("DELETE FROM userinfo")
    fun deleteAll()

}